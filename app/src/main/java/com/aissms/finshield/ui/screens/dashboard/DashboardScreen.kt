package com.aissms.finshield.ui.screens.dashboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aissms.finshield.model.AppLanguage
import com.aissms.finshield.model.MockLoanData
import com.aissms.finshield.ui.components.ActionCard
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldCard
import com.aissms.finshield.ui.components.FinShieldHeader
import com.aissms.finshield.ui.components.MockNoticeBanner
import com.aissms.finshield.ui.components.RiskBadge
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    selectedLanguage: AppLanguage,
    onLanguageClick: () -> Unit,
    onNavigateTo: (String) -> Unit
) {
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            FinShieldHeader(
                selectedLanguage = selectedLanguage,
                onLanguageClick = onLanguageClick
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Mock Notice Banner
            MockNoticeBanner(
                message = "IEEE Synapse Hackathon Demo — Synthetic contract models active"
            )

            // Welcome Card with Protection Badge
            FinShieldCard(
                backgroundColor = PrimaryNavy,
                borderColor = PrimaryNavy
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "FinShield Protection Active",
                                style = MaterialTheme.typography.labelMedium,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Welcome, Alex!",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Scan or upload your loan agreement before signing.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            // Primary Feature Callout: Analyze Loan Agreement
            Text(
                text = "Primary Risk Tool",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            ActionCard(
                title = "Analyze Loan Agreement",
                subtitle = "Scan PDF, contract images, or paste loan terms to detect hidden clauses",
                icon = Icons.Default.DocumentScanner,
                onClick = { onNavigateTo(Screen.LoanAnalysis.route) },
                badgeText = "AI Powered"
            )

            // Secondary Quick Tools Grid Title
            Text(
                text = "Financial Tools & Guidance",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            ActionCard(
                title = "Loan EMI Calculator",
                subtitle = "Compute true monthly payments, interest totals, and hidden cost impacts",
                icon = Icons.Default.Calculate,
                onClick = { onNavigateTo(Screen.LoanCalculator.route) }
            )

            ActionCard(
                title = "Compare Bank Loans",
                subtitle = "Side-by-side evaluation of loan offers to highlight the best net savings",
                icon = Icons.Default.CompareArrows,
                onClick = { onNavigateTo(Screen.LoanComparison.route) }
            )

            ActionCard(
                title = "Credit Score Guidance",
                subtitle = "Understand how loan penalties and default terms impact your credit profile",
                icon = Icons.Default.Security,
                onClick = { onNavigateTo(Screen.CreditGuidance.route) }
            )

            // Recent Analysis Placeholder Section
            Text(
                text = "Recent Analysis History",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            MockLoanData.sampleContracts.forEach { sample ->
                FinShieldCard(
                    onClick = { onNavigateTo(Screen.LoanAnalysis.route) }
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = null,
                                    tint = PrimaryNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = sample.title,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "${sample.bankName} • ${sample.loanType}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = sample.sampleTextSnippet,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                maxLines = 2
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        RiskBadge(riskLevel = sample.riskLevel)
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
