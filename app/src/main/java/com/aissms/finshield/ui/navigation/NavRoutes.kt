package com.aissms.finshield.ui.navigation

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object LanguageSelection : Screen("language_selection")
    object Onboarding : Screen("onboarding")
    object Auth : Screen("auth")
    object Dashboard : Screen("dashboard")
    object LoanAnalysis : Screen("loan_analysis")
    object LoanCalculator : Screen("loan_calculator")
    object LoanComparison : Screen("loan_comparison")
    object CreditGuidance : Screen("credit_guidance")
}
