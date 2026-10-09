package com.aissms.finshield.ui.screens.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.model.AppLanguage
import com.aissms.finshield.ui.components.DashboardFigmaHeader
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.HeroAnalyzeCard
import com.aissms.finshield.ui.components.MockNoticeBanner
import com.aissms.finshield.ui.components.QuickActionCard
import com.aissms.finshield.ui.components.RecentAnalysisCard
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.IconBoxAmberBg
import com.aissms.finshield.ui.theme.IconBoxAmberTint
import com.aissms.finshield.ui.theme.IconBoxBlueBg
import com.aissms.finshield.ui.theme.IconBoxBlueTint
import com.aissms.finshield.ui.theme.IconBoxGreenBg
import com.aissms.finshield.ui.theme.IconBoxGreenTint
import com.aissms.finshield.ui.theme.TextPrimary

@Composable
fun DashboardScreen(
    selectedLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            DashboardFigmaHeader(
                userName = "Ananya",
                onNotificationClick = { },
                onProfileClick = onLanguageClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.Dashboard.route,
                onNavigate = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            // IEEE Hackathon Notice Banner
            MockNoticeBanner(
                message = "IEEE Synapse Hackathon Demo Mode — Synthetic contract models active"
            )

            // Primary Featured Hero Card (Figma Reference Image 2)
            HeroAnalyzeCard(
                onAnalyzeClick = { onNavigateTo(Screen.LoanAnalysisUpload.route) }
            )

            // Quick actions Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Quick actions",
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickActionCard(
                        title = "Loan\nCalculator",
                        subtitle = "Verify your EMI",
                        icon = Icons.Default.Calculate,
                        iconBgColor = IconBoxGreenBg,
                        iconTintColor = IconBoxGreenTint,
                        onClick = { onNavigateTo(Screen.LoanCalculator.route) },
                        modifier = Modifier.weight(1f)
                    )

                    QuickActionCard(
                        title = "Compare\nLoans",
                        subtitle = "Find better value",
                        icon = Icons.AutoMirrored.Filled.CompareArrows,
                        iconBgColor = IconBoxBlueBg,
                        iconTintColor = IconBoxBlueTint,
                        onClick = { onNavigateTo(Screen.LoanComparison.route) },
                        modifier = Modifier.weight(1f)
                    )

                    QuickActionCard(
                        title = "Credit Score\nGuidance",
                        subtitle = "Improve profile",
                        icon = Icons.Default.AutoAwesome,
                        iconBgColor = IconBoxAmberBg,
                        iconTintColor = IconBoxAmberTint,
                        onClick = { onNavigateTo(Screen.CreditGuidance.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Recent analyses Section
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recent analyses",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )

                    Text(
                        text = "View all",
                        style = MaterialTheme.typography.labelMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        modifier = Modifier.clickable { onNavigateTo(Screen.RecentAnalyses.route) }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                RecentAnalysisCard(
                    fileName = "HDFC_Personal_Loan.pdf",
                    reviewMeta = "Reviewed 18 May · 12 pages",
                    badgeText = "2 to review",
                    onClick = { onNavigateTo(Screen.LoanAnalysisReport.route) }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
