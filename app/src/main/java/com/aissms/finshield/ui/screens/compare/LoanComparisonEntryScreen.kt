package com.aissms.finshield.ui.screens.compare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CompareArrows
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
import com.aissms.finshield.model.LoanOfferItem
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldCard
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.MockNoticeBanner
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LoanComparisonEntryScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val offerA = LoanOfferItem(
        bankName = "Bank A (Sample)",
        principalAmount = 500000.0,
        annualInterestRate = 10.5,
        tenureMonths = 36,
        processingFeePercent = 1.0
    )

    val offerB = LoanOfferItem(
        bankName = "Bank B (Sample)",
        principalAmount = 500000.0,
        annualInterestRate = 12.0,
        tenureMonths = 36,
        processingFeePercent = 2.5
    )

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    val savingsAmount = offerB.totalCostOfLoan - offerA.totalCostOfLoan

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Compare Loan Offers",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanComparison.route,
                onNavigate = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MockNoticeBanner(
                message = "Smart Loan Comparison Matrix — Highlighting hidden cost differences & net savings"
            )

            // Recommendation Banner
            FinShieldCard(
                backgroundColor = PrimaryNavy,
                borderColor = PrimaryNavy
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "FinShield Recommendation",
                            style = MaterialTheme.typography.labelMedium,
                            color = EmeraldGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${offerA.bankName} saves you ${currencyFormatter.format(savingsAmount)} over the loan tenure!",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "Side-by-Side Offer Matrix",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            // Side-by-side Card Comparison
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Bank A Card
                FinShieldCard(
                    modifier = Modifier.weight(1f),
                    borderColor = EmeraldGreen
                ) {
                    Surface(
                        color = EmeraldGreen,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "RECOMMENDED",
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = offerA.bankName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MetricRow(label = "Interest Rate", value = "${offerA.annualInterestRate}%")
                    MetricRow(label = "Monthly EMI", value = currencyFormatter.format(offerA.state.monthlyEmi))
                    MetricRow(label = "Processing Fee", value = "${offerA.processingFeePercent}%")
                    MetricRow(label = "Total Interest", value = currencyFormatter.format(offerA.state.totalInterest))
                    MetricRow(label = "Net Outflow", value = currencyFormatter.format(offerA.totalCostOfLoan), isHighlight = true)
                }

                // Bank B Card
                FinShieldCard(
                    modifier = Modifier.weight(1f)
                ) {
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = offerB.bankName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    MetricRow(label = "Interest Rate", value = "${offerB.annualInterestRate}%")
                    MetricRow(label = "Monthly EMI", value = currencyFormatter.format(offerB.state.monthlyEmi))
                    MetricRow(label = "Processing Fee", value = "${offerB.processingFeePercent}%")
                    MetricRow(label = "Total Interest", value = currencyFormatter.format(offerB.state.totalInterest))
                    MetricRow(label = "Net Outflow", value = currencyFormatter.format(offerB.totalCostOfLoan), isHighlight = false)
                }
            }
        }
    }
}

@Composable
private fun MetricRow(
    label: String,
    value: String,
    isHighlight: Boolean = false
) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelMedium, color = TextSecondary)
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = if (isHighlight) FontWeight.Bold else FontWeight.SemiBold,
            color = if (isHighlight) EmeraldGreen else TextPrimary
        )
    }
}
