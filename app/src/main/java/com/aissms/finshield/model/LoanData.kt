package com.aissms.finshield.model

import kotlin.math.pow
import kotlin.math.roundToInt

enum class RiskLevel(val label: String) {
    LOW("Low Risk"),
    MEDIUM("Moderate Risk"),
    HIGH("High Risk / Alert")
}

enum class LoanCategory(val displayName: String) {
    PERSONAL("Personal Loan"),
    HOME("Home Loan"),
    EDUCATION("Education Loan"),
    VEHICLE("Vehicle Loan")
}

enum class InterestType(val label: String) {
    REDUCING_BALANCE("Reducing Balance"),
    FLAT_RATE("Flat Rate")
}

data class SampleLoanContract(
    val id: String,
    val title: String,
    val bankName: String,
    val loanType: String,
    val sampleTextSnippet: String,
    val riskLevel: RiskLevel,
    val flagCount: Int
)

data class CalculatorState(
    val principalAmount: Double = 500000.0,
    val interestRate: Double = 10.5,
    val tenureMonths: Int = 36
) {
    val monthlyRate: Double
        get() = (interestRate / 12.0) / 100.0

    val monthlyEmi: Double
        get() {
            if (monthlyRate == 0.0) return principalAmount / tenureMonths
            val pow = (1.0 + monthlyRate).pow(tenureMonths.toDouble())
            return (principalAmount * monthlyRate * pow) / (pow - 1.0)
        }

    val totalPayable: Double
        get() = monthlyEmi * tenureMonths

    val totalInterest: Double
        get() = totalPayable - principalAmount
}

data class LenderOffer(
    val id: String,
    val lenderName: String,
    val category: LoanCategory,
    val annualInterestRate: Double,
    val interestType: InterestType = InterestType.REDUCING_BALANCE,
    val processingFeePercent: Double = 1.0,
    val fixedProcessingFee: Double = 0.0,
    val minPrincipal: Double = 50000.0,
    val maxPrincipal: Double = 5000000.0,
    val officialDetailsNote: String = "Official terms available on bank portal."
) {
    fun calculateMetrics(principal: Double, tenureMonths: Int): LenderOfferMetrics {
        val fee = fixedProcessingFee + (principal * (processingFeePercent / 100.0))

        val monthlyEmi: Double
        val totalInterest: Double
        val totalRepayment: Double

        if (interestType == InterestType.REDUCING_BALANCE) {
            val monthlyRate = (annualInterestRate / 12.0) / 100.0
            if (monthlyRate == 0.0) {
                monthlyEmi = principal / tenureMonths
            } else {
                val pow = (1.0 + monthlyRate).pow(tenureMonths.toDouble())
                monthlyEmi = (principal * monthlyRate * pow) / (pow - 1.0)
            }
            totalRepayment = monthlyEmi * tenureMonths
            totalInterest = totalRepayment - principal
        } else {
            // Flat Rate Calculation
            totalInterest = principal * (annualInterestRate / 100.0) * (tenureMonths / 12.0)
            totalRepayment = principal + totalInterest
            monthlyEmi = totalRepayment / tenureMonths
        }

        val estimatedTotalCost = totalRepayment + fee
        val years = tenureMonths / 12.0
        val annualizedFeeRate = if (years > 0) (fee / principal) / years * 100 else 0.0
        val indicativeApr = (annualInterestRate + annualizedFeeRate * 1.1)

        return LenderOfferMetrics(
            lenderName = lenderName,
            category = category,
            annualInterestRate = annualInterestRate,
            interestType = interestType,
            processingFee = fee,
            monthlyEmi = (monthlyEmi * 100).roundToInt() / 100.0,
            totalInterest = (totalInterest * 100).roundToInt() / 100.0,
            totalRepayment = (totalRepayment * 100).roundToInt() / 100.0,
            estimatedTotalCost = (estimatedTotalCost * 100).roundToInt() / 100.0,
            indicativeApr = (indicativeApr * 10).roundToInt() / 10.0,
            officialDetailsNote = officialDetailsNote
        )
    }
}

data class LenderOfferMetrics(
    val lenderName: String,
    val category: LoanCategory,
    val annualInterestRate: Double,
    val interestType: InterestType,
    val processingFee: Double,
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalRepayment: Double,
    val estimatedTotalCost: Double,
    val indicativeApr: Double,
    val officialDetailsNote: String
)

data class LoanOfferItem(
    val bankName: String,
    val principalAmount: Double,
    val annualInterestRate: Double,
    val tenureMonths: Int,
    val processingFeePercent: Double
) {
    val state = CalculatorState(principalAmount, annualInterestRate, tenureMonths)
    val totalProcessingFee: Double = principalAmount * (processingFeePercent / 100.0)
    val totalCostOfLoan: Double = state.totalPayable + totalProcessingFee
}

object MockLoanData {
    val lenderCatalog = listOf(
        // Personal Loans
        LenderOffer("p1", "HDFC Bank", LoanCategory.PERSONAL, 10.5, InterestType.REDUCING_BALANCE, 1.0, 1000.0, officialDetailsNote = "Verified sample rates from HDFC Personal Loan scheme."),
        LenderOffer("p2", "State Bank of India", LoanCategory.PERSONAL, 11.1, InterestType.REDUCING_BALANCE, 0.5, 500.0, officialDetailsNote = "SBI Xpress Credit Personal Loan scheme."),
        LenderOffer("p3", "ICICI Bank", LoanCategory.PERSONAL, 10.85, InterestType.REDUCING_BALANCE, 1.25, 0.0, officialDetailsNote = "ICICI Instant Personal Loan scheme."),
        LenderOffer("p4", "Axis Bank", LoanCategory.PERSONAL, 11.5, InterestType.FLAT_RATE, 1.5, 0.0, officialDetailsNote = "Axis Bank Fixed Rate Personal Loan scheme."),

        // Home Loans
        LenderOffer("h1", "State Bank of India", LoanCategory.HOME, 8.4, InterestType.REDUCING_BALANCE, 0.35, 2000.0, officialDetailsNote = "SBI Regular Home Loan scheme."),
        LenderOffer("h2", "HDFC Bank", LoanCategory.HOME, 8.5, InterestType.REDUCING_BALANCE, 0.5, 3000.0, officialDetailsNote = "HDFC Home Loan scheme."),
        LenderOffer("h3", "ICICI Bank", LoanCategory.HOME, 8.75, InterestType.REDUCING_BALANCE, 0.5, 2500.0, officialDetailsNote = "ICICI Express Home Loan scheme."),
        LenderOffer("h4", "Bank of Baroda", LoanCategory.HOME, 8.4, InterestType.REDUCING_BALANCE, 0.25, 1500.0, officialDetailsNote = "Baroda Home Loan Advantage scheme."),

        // Education Loans
        LenderOffer("e1", "State Bank of India", LoanCategory.EDUCATION, 9.5, InterestType.REDUCING_BALANCE, 0.0, 0.0, officialDetailsNote = "SBI Scholar Education Loan scheme."),
        LenderOffer("e2", "Canara Bank", LoanCategory.EDUCATION, 9.8, InterestType.REDUCING_BALANCE, 0.0, 500.0, officialDetailsNote = "Canara Vidya Turant Scheme."),
        LenderOffer("e3", "HDFC Credila", LoanCategory.EDUCATION, 10.25, InterestType.REDUCING_BALANCE, 1.0, 2000.0, officialDetailsNote = "HDFC Credila Specialist Higher Education Loan."),

        // Vehicle Loans
        LenderOffer("v1", "State Bank of India", LoanCategory.VEHICLE, 8.85, InterestType.REDUCING_BALANCE, 0.0, 1000.0, officialDetailsNote = "SBI New Car Loan scheme."),
        LenderOffer("v2", "HDFC Bank", LoanCategory.VEHICLE, 9.1, InterestType.REDUCING_BALANCE, 0.5, 1500.0, officialDetailsNote = "HDFC Express Auto Loan scheme."),
        LenderOffer("v3", "ICICI Bank", LoanCategory.VEHICLE, 9.25, InterestType.FLAT_RATE, 0.75, 1000.0, officialDetailsNote = "ICICI Instant Car Loan scheme.")
    )

    val sampleContracts = listOf(
        SampleLoanContract(
            id = "c1",
            title = "Personal Loan Agreement 2026",
            bankName = "HDFC Bank (Sample)",
            loanType = "Personal Loan",
            sampleTextSnippet = "Clause 14.2: Borrower agrees to 4.5% foreclosure penalty on principal outstanding if repaid within 24 months. Floating rate adjusted semi-annually without prior notice.",
            riskLevel = RiskLevel.MEDIUM,
            flagCount = 3
        ),
        SampleLoanContract(
            id = "c2",
            title = "Home Mortgage Contract Offer",
            bankName = "State Bank of India (Sample)",
            loanType = "Home Loan",
            sampleTextSnippet = "Clause 8.1: Mandatory credit shield insurance premium of ₹45,000 auto-debited at disbursement. Prepayment allowed after 12 EMIs with 0% penalty.",
            riskLevel = RiskLevel.LOW,
            flagCount = 1
        ),
        SampleLoanContract(
            id = "c3",
            title = "Instant Credit App Loan Terms",
            bankName = "FlexiCredit App (Sample)",
            loanType = "Micro Credit",
            sampleTextSnippet = "Clause 22: Daily overdue penal interest of 0.2% per day. Processing fee of 5% deducted upfront from principal amount. Arbitration in remote jurisdiction.",
            riskLevel = RiskLevel.HIGH,
            flagCount = 6
        )
    )
}
