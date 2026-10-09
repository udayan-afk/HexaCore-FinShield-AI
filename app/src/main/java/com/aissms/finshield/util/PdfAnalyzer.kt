package com.aissms.finshield.util

import android.content.Context
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.OpenableColumns
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.zip.InflaterInputStream
import kotlin.math.pow
import kotlin.math.roundToInt

data class LoanAnalysisData(
    val fileName: String,
    val isSuccess: Boolean,
    val statusMessage: String,
    val pageCount: Int = 0,
    val extractedText: String = "",
    val principalAmount: Double? = null,
    val interestRate: Double? = null,
    val tenureMonths: Int? = null,
    val statedEmi: Double? = null,
    val statedTotalRepayment: Double? = null,
    val calculatedEmi: Double? = null,
    val calculatedTotalRepayment: Double? = null,
    val discrepancyAmount: Double? = null,
    val discrepancyMessage: String? = null,
    val processingFee: String? = null,
    val prepaymentCharges: String? = null,
    val latePaymentPenalty: String? = null,
    val missingFields: List<String> = emptyList(),
    val extractedClauses: List<Pair<String, String>> = emptyList()
)

object PdfAnalyzer {

    fun analyzePdfUri(context: Context, uri: Uri, customFileName: String? = null): LoanAnalysisData {
        val fileName = customFileName ?: getFileNameFromUri(context, uri)

        // Special handling for built-in sample agreement
        if (uri.toString().startsWith("sample://")) {
            return analyzeSampleAgreement(fileName)
        }

        var pfd: ParcelFileDescriptor? = null
        var renderer: PdfRenderer? = null
        var pageCount = 0

        try {
            pfd = context.contentResolver.openFileDescriptor(uri, "r")
            if (pfd == null) {
                return LoanAnalysisData(
                    fileName = fileName,
                    isSuccess = false,
                    statusMessage = "Unable to open file stream from device storage."
                )
            }

            try {
                renderer = PdfRenderer(pfd)
                pageCount = renderer.pageCount
            } catch (e: SecurityException) {
                return LoanAnalysisData(
                    fileName = fileName,
                    isSuccess = false,
                    statusMessage = "Encrypted PDF detected. Please remove password protection before analysis.",
                    pageCount = 0
                )
            } catch (e: Exception) {
                return LoanAnalysisData(
                    fileName = fileName,
                    isSuccess = false,
                    statusMessage = "Invalid or corrupted PDF document structure.",
                    pageCount = 0
                )
            } finally {
                renderer?.close()
            }

            // Extract raw text from PDF stream
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: return LoanAnalysisData(fileName = fileName, isSuccess = false, statusMessage = "Cannot read PDF content.")

            val rawBytes = inputStream.use { it.readBytes() }
            val extractedText = extractTextFromPdfBytes(rawBytes)

            if (extractedText.isBlank()) {
                return LoanAnalysisData(
                    fileName = fileName,
                    isSuccess = false,
                    pageCount = pageCount,
                    statusMessage = "Scanned or image-only PDF detected. No embedded text layer found."
                )
            }

            return analyzeExtractedText(fileName = fileName, pageCount = pageCount, text = extractedText)

        } catch (e: Exception) {
            return LoanAnalysisData(
                fileName = fileName,
                isSuccess = false,
                statusMessage = "Failed to analyze document: ${e.localizedMessage ?: "Unknown error"}"
            )
        }
    }

    private fun getFileNameFromUri(context: Context, uri: Uri): String {
        var result = "Document.pdf"
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) {
                result = cursor.getString(nameIndex)
            }
        }
        return result
    }

    private fun extractTextFromPdfBytes(bytes: ByteArray): String {
        val textBuilder = StringBuilder()
        val contentString = String(bytes, Charsets.ISO_8859_1)

        // Match uncompressed BT...ET blocks
        val btEtRegex = Regex("""BT\s+(.*?)\s+ET""", RegexOption.DOT_MATCHES_ALL)
        for (match in btEtRegex.findAll(contentString)) {
            val block = match.groupValues[1]
            val stringMatches = Regex("""\((.*?)\)\s*T[jJ]""").findAll(block)
            for (sm in stringMatches) {
                textBuilder.append(sm.groupValues[1]).append(" ")
            }
        }

        // If direct BT/ET produced text, return it
        if (textBuilder.length > 30) {
            return textBuilder.toString().trim()
        }

        // Try extracting FlateDecode streams
        val streamRegex = Regex("""stream\r?\n(.*?)\r?\nendstream""", RegexOption.DOT_MATCHES_ALL)
        for (match in streamRegex.findAll(contentString)) {
            val streamContent = match.groupValues[1]
            try {
                val streamBytes = streamContent.toByteArray(Charsets.ISO_8859_1)
                val decompressed = decompressFlate(streamBytes)
                val decompressedStr = String(decompressed, Charsets.UTF_8)

                val innerBtMatches = Regex("""\((.*?)\)\s*T[jJ]""").findAll(decompressedStr)
                for (sm in innerBtMatches) {
                    textBuilder.append(sm.groupValues[1]).append(" ")
                }
            } catch (_: Exception) {
                // Ignore invalid stream inflations
            }
        }

        return textBuilder.toString().trim()
    }

    private fun decompressFlate(bytes: ByteArray): ByteArray {
        val out = ByteArrayOutputStream()
        val inflater = InflaterInputStream(bytes.inputStream())
        val buffer = ByteArray(1024)
        var count: Int
        while (inflater.read(buffer).also { count = it } != -1) {
            out.write(buffer, 0, count)
        }
        return out.toByteArray()
    }

    fun analyzeExtractedText(fileName: String, pageCount: Int, text: String): LoanAnalysisData {
        val cleanText = text.replace(Regex("""\s+"""), " ")

        // Extract Principal
        val principal = extractDoublePattern(cleanText, listOf(
            Regex("""(?:Loan\s+Amount|Principal|Sanctioned\s+Amount|Amount)\s*[:=]?\s*₹?\s*([\d,]+(?:\.\d+)?)""", RegexOption.IGNORE_CASE),
            Regex("""₹\s*([\d,]{5,10})""")
        ))

        // Extract Interest Rate
        val rate = extractDoublePattern(cleanText, listOf(
            Regex("""(?:Interest\s+Rate|Rate\s+of\s+Interest|ROI)\s*[:=]?\s*([\d.]+)\s*%""", RegexOption.IGNORE_CASE),
            Regex("""([\d.]+)\s*%\s*(?:p\.a\.|per\s+annum)""", RegexOption.IGNORE_CASE)
        ))

        // Extract Tenure
        val tenure = extractIntPattern(cleanText, listOf(
            Regex("""(?:Tenure|Duration|Period)\s*[:=]?\s*(\d+)\s*(?:months|mths)""", RegexOption.IGNORE_CASE),
            Regex("""(\d+)\s*(?:months|mths)""", RegexOption.IGNORE_CASE)
        ))

        // Extract Stated EMI
        val statedEmi = extractDoublePattern(cleanText, listOf(
            Regex("""(?:EMI|Monthly\s+Installment|Monthly\s+Payment)\s*[:=]?\s*₹?\s*([\d,]+(?:\.\d+)?)""", RegexOption.IGNORE_CASE)
        ))

        // Extract Stated Total Repayment
        val statedTotalRepayment = extractDoublePattern(cleanText, listOf(
            Regex("""(?:Total\s+Repayment|Total\s+Amount\s+Payable)\s*[:=]?\s*₹?\s*([\d,]+(?:\.\d+)?)""", RegexOption.IGNORE_CASE)
        ))

        val missing = mutableListOf<String>()
        if (principal == null) missing.add("Principal Loan Amount")
        if (rate == null) missing.add("Annual Interest Rate")
        if (tenure == null) missing.add("Loan Tenure")

        var calculatedEmi: Double? = null
        var calculatedTotalRepayment: Double? = null
        var discrepancyAmount: Double? = null
        var discrepancyMsg: String? = null

        if (principal != null && rate != null && tenure != null && tenure > 0) {
            val monthlyRate = rate / (12 * 100)
            val emiRaw = (principal * monthlyRate * (1 + monthlyRate).pow(tenure)) /
                    ((1 + monthlyRate).pow(tenure) - 1)
            calculatedEmi = (emiRaw * 100).roundToInt() / 100.0
            calculatedTotalRepayment = (calculatedEmi * tenure * 100).roundToInt() / 100.0

            if (statedTotalRepayment != null) {
                val diff = (statedTotalRepayment - calculatedTotalRepayment)
                if (kotlin.math.abs(diff) > 100) {
                    discrepancyAmount = (diff * 100).roundToInt() / 100.0
                    discrepancyMsg = "Agreement schedule states total repayment as ₹${formatCurrency(statedTotalRepayment)}, which differs by ₹${formatCurrency(kotlin.math.abs(diff))} from calculated standard EMI terms."
                }
            } else if (statedEmi != null) {
                val diff = (statedEmi - calculatedEmi)
                if (kotlin.math.abs(diff) > 50) {
                    discrepancyAmount = (diff * 100).roundToInt() / 100.0
                    discrepancyMsg = "Stated EMI of ₹${formatCurrency(statedEmi)} differs by ₹${formatCurrency(kotlin.math.abs(diff))} from calculated EMI of ₹${formatCurrency(calculatedEmi)}."
                }
            }
        }

        val clauses = mutableListOf<Pair<String, String>>()

        // Processing Fee
        val processingFeeMatch = Regex("""Processing\s+Fee\s*[:=]?\s*([^\n.;]{4,60})""", RegexOption.IGNORE_CASE).find(cleanText)?.groupValues?.get(1)
        val processingFeeStr = processingFeeMatch ?: "Standard processing charges apply"
        clauses.add("Processing Fee" to processingFeeStr)

        // Prepayment Charges
        val prepaymentMatch = Regex("""Prepayment\s*[:=]?\s*([^\n.;]{4,60})""", RegexOption.IGNORE_CASE).find(cleanText)?.groupValues?.get(1)
        val prepaymentStr = prepaymentMatch ?: "Subject to lender foreclosure terms"
        clauses.add("Prepayment Charges" to prepaymentStr)

        // Late Payment Fee
        val lateFeeMatch = Regex("""Late\s+(?:Payment|Fee)\s*[:=]?\s*([^\n.;]{4,60})""", RegexOption.IGNORE_CASE).find(cleanText)?.groupValues?.get(1)
        val lateFeeStr = lateFeeMatch ?: "Standard penal interest on default"
        clauses.add("Late Payment Penalties" to lateFeeStr)

        val isSuccess = missing.size < 3

        return LoanAnalysisData(
            fileName = fileName,
            isSuccess = isSuccess,
            statusMessage = if (isSuccess) "Analysis completed successfully." else "Could not extract sufficient financial terms from PDF.",
            pageCount = pageCount,
            extractedText = cleanText,
            principalAmount = principal,
            interestRate = rate,
            tenureMonths = tenure,
            statedEmi = statedEmi,
            statedTotalRepayment = statedTotalRepayment,
            calculatedEmi = calculatedEmi,
            calculatedTotalRepayment = calculatedTotalRepayment,
            discrepancyAmount = discrepancyAmount,
            discrepancyMessage = discrepancyMsg,
            processingFee = processingFeeStr,
            prepaymentCharges = prepaymentStr,
            latePaymentPenalty = lateFeeStr,
            missingFields = missing,
            extractedClauses = clauses
        )
    }

    private fun analyzeSampleAgreement(fileName: String): LoanAnalysisData {
        return LoanAnalysisData(
            fileName = fileName,
            isSuccess = true,
            statusMessage = "Review complete — 1 potential repayment discrepancy detected.",
            pageCount = 12,
            extractedText = "Sample HDFC Personal Loan Agreement",
            principalAmount = 500000.0,
            interestRate = 12.5,
            tenureMonths = 36,
            statedEmi = 16933.0,
            statedTotalRepayment = 609600.0,
            calculatedEmi = 16727.0,
            calculatedTotalRepayment = 602172.0,
            discrepancyAmount = 7428.0,
            discrepancyMessage = "Agreement schedule states total repayment as ₹6,09,600, which differs by ₹7,428 from calculated standard EMI terms.",
            processingFee = "₹2,500 + GST (Deducted upfront)",
            prepaymentCharges = "2% + GST after 6 EMIs",
            latePaymentPenalty = "₹500 per default + 24% p.a. penal interest",
            extractedClauses = listOf(
                "Prepayment Charges" to "2% + GST after completion of 6 EMIs",
                "Processing Fee" to "₹2,500 + GST (Deducted upfront from disbursal)",
                "Late Payment Penalties" to "₹500 per default + 24% p.a. penal interest"
            )
        )
    }

    private fun extractDoublePattern(text: String, regexes: List<Regex>): Double? {
        for (regex in regexes) {
            val match = regex.find(text)
            if (match != null) {
                val strVal = match.groupValues[1].replace(",", "").trim()
                val parsed = strVal.toDoubleOrNull()
                if (parsed != null && parsed > 0) return parsed
            }
        }
        return null
    }

    private fun extractIntPattern(text: String, regexes: List<Regex>): Int? {
        for (regex in regexes) {
            val match = regex.find(text)
            if (match != null) {
                val strVal = match.groupValues[1].trim()
                val parsed = strVal.toIntOrNull()
                if (parsed != null && parsed > 0) return parsed
            }
        }
        return null
    }

    private fun formatCurrency(amount: Double): String {
        val longVal = amount.toLong()
        val str = longVal.toString()
        if (str.length <= 3) return str
        val lastThree = str.substring(str.length - 3)
        val otherNumbers = str.substring(0, str.length - 3)
        val formatted = otherNumbers.reversed().chunked(2).joinToString(",").reversed()
        return "$formatted,$lastThree"
    }
}
