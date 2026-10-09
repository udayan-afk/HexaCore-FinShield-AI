package com.aissms.finshield.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.aissms.finshield.model.AppLanguage
import com.aissms.finshield.ui.screens.analyze.LoanAnalysisUploadScreen
import com.aissms.finshield.ui.screens.analyze.LoanReportScreen
import com.aissms.finshield.ui.screens.auth.AuthScreen
import com.aissms.finshield.ui.screens.calculator.LoanCalculatorEntryScreen
import com.aissms.finshield.ui.screens.compare.LoanComparisonEntryScreen
import com.aissms.finshield.ui.screens.dashboard.DashboardScreen
import com.aissms.finshield.ui.screens.guidance.CreditScoreGuidanceScreen
import com.aissms.finshield.ui.screens.language.LanguageSelectionScreen
import com.aissms.finshield.ui.screens.onboarding.OnboardingScreen
import com.aissms.finshield.ui.screens.profile.ProfileScreen
import com.aissms.finshield.ui.screens.recent.RecentAnalysesScreen
import com.aissms.finshield.ui.screens.splash.SplashScreen

@Composable
fun FinShieldNavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    var currentLanguage by remember { mutableStateOf(AppLanguage.ENGLISH) }
    var currentAnalysisData by remember { mutableStateOf<com.aissms.finshield.util.LoanAnalysisData?>(null) }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateNext = {
                    navController.navigate(Screen.LanguageSelection.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.LanguageSelection.route) {
            LanguageSelectionScreen(
                currentLanguage = currentLanguage,
                onLanguageSelected = { currentLanguage = it },
                onContinue = {
                    navController.navigate(Screen.Onboarding.route)
                }
            )
        }

        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onFinishOnboarding = {
                    navController.navigate(Screen.Auth.route)
                }
            )
        }

        composable(Screen.Auth.route) {
            AuthScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                selectedLanguage = currentLanguage,
                onLanguageClick = {
                    navController.navigate(Screen.LanguageSelection.route)
                },
                onNavigateTo = { route ->
                    if (route != Screen.Dashboard.route) {
                        navController.navigate(route)
                    }
                }
            )
        }

        composable(Screen.LoanAnalysisUpload.route) {
            LoanAnalysisUploadScreen(
                onNavigateToReportWithData = { data ->
                    currentAnalysisData = data
                    navController.navigate(Screen.LoanAnalysisReport.route)
                },
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.LoanAnalysisReport.route) {
            LoanReportScreen(
                analysisData = currentAnalysisData,
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.LoanCalculator.route) {
            LoanCalculatorEntryScreen(
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.LoanComparison.route) {
            LoanComparisonEntryScreen(
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.CreditGuidance.route) {
            CreditScoreGuidanceScreen(
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.RecentAnalyses.route) {
            RecentAnalysesScreen(
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateTo = { route ->
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route)
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
