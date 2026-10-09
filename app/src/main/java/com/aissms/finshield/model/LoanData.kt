package com.aissms.finshield.model

enum class RiskLevel(val label: String) {
    LOW("Low Risk"),
    MEDIUM("Moderate Risk"),
    HIGH("High Risk / Alert")
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
            val pow = Math.pow(1.0 + monthlyRate, tenureMonths.toDouble())
            return (principalAmount * monthlyRate * pow) / (pow - 1.0)
        }

    val totalPayable: Double
        get() = monthlyEmi * tenureMonths

    val totalInterest: Double
        get() = totalPayable - principalAmount
}

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
