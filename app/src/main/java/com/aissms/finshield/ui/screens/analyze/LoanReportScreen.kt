package com.aissms.finshield.ui.screens.analyze

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.sp
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.BadgeReviewAmberBg
import com.aissms.finshield.ui.theme.BadgeReviewAmberBorder
import com.aissms.finshield.ui.theme.BadgeReviewAmberText
import com.aissms.finshield.ui.theme.BorderNeutral
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.IconBoxGreenBg
import com.aissms.finshield.ui.theme.IconBoxRedBg
import com.aissms.finshield.ui.theme.IconBoxRedTint
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.SurfaceCard
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary
import com.aissms.finshield.util.LoanAnalysisData
import java.text.NumberFormat
import java.util.Locale

@Composable
fun LoanReportScreen(
    analysisData: LoanAnalysisData? = null,
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val data = analysisData ?: LoanAnalysisData(
        fileName = "HDFC_Personal_Loan.pdf",
        isSuccess = true,
        statusMessage = "Review complete — 1 potential repayment discrepancy detected.",
        pageCount = 12,
        principalAmount = 500000.0,
        interestRate = 12.5,
        tenureMonths = 36,
        calculatedEmi = 16727.0,
        calculatedTotalRepayment = 602172.0,
        discrepancyMessage = "Agreement schedule states total repayment as ₹6,09,600, which differs by ₹7,428 from calculated standard EMI terms.",
        extractedClauses = listOf(
            "Prepayment Charges" to "2% + GST after completion of 6 EMIs",
            "Processing Fee" to "₹2,500 + GST (Deducted upfront from disbursal)",
            "Late Payment Penalties" to "₹500 per default + 24% p.a. penal interest"
        )
    )

    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
        maximumFractionDigits = 0
    }

    val principalStr = data.principalAmount?.let { currencyFormatter.format(it) } ?: "Not Extracted"
    val rateStr = data.interestRate?.let { "$it% p.a." } ?: "Not Extracted"
    val tenureStr = data.tenureMonths?.let { "$it months" } ?: "Not Extracted"
    val emiStr = data.calculatedEmi?.let { currencyFormatter.format(it) } ?: "Uncalculated"
    val totalRepaymentStr = data.calculatedTotalRepayment?.let { currencyFormatter.format(it) } ?: "Uncalculated"

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Analysis Report",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanAnalysisReport.route,
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
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Stepper Header (Step 3 Active)
            AnalysisStepper(currentStep = 3)

            Spacer(modifier = Modifier.height(16.dp))

            // Banner
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (data.isSuccess) IconBoxGreenBg else IconBoxRedBg.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (data.isSuccess) Icons.Default.CheckCircle else Icons.Default.Info,
                        contentDescription = null,
                        tint = if (data.isSuccess) EmeraldGreen else IconBoxRedTint,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (data.isSuccess) "Review complete" else "Analysis Notice",
                            style = MaterialTheme.typography.titleSmall,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = data.statusMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Loan Snapshot Card (Figma Image 5)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, BorderNeutral)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Loan Snapshot",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = PrimaryNavy.copy(alpha = 0.08f)
                        ) {
                            Text(
                                text = "Extracted terms",
                                color = PrimaryNavy,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 2x2 Grid of Terms
                    Row(modifier = Modifier.fillMaxWidth()) {
                        SnapshotDetailItem(
                            label = "Principal",
                            value = principalStr,
                            modifier = Modifier.weight(1f)
                        )
                        SnapshotDetailItem(
                            label = "Interest Rate",
                            value = rateStr,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth()) {
                        SnapshotDetailItem(
                            label = "Tenure",
                            value = tenureStr,
                            modifier = Modifier.weight(1f)
                        )
                        SnapshotDetailItem(
                            label = "Calculated EMI",
                            value = emiStr,
                            valueColor = EmeraldGreen,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderNeutral))
                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Estimated Total Repayment",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = totalRepaymentStr,
                                style = MaterialTheme.typography.titleLarge,
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }

                        Row(
                            modifier = Modifier
                                .clickable { onNavigateTo(Screen.LoanCalculator.route) }
                                .padding(4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Verify in calculator",
                                style = MaterialTheme.typography.labelMedium,
                                color = EmeraldGreen,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }
                }
            }

            // Discrepancy Risk Amber Card (if present)
            if (!data.discrepancyMessage.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = BadgeReviewAmberBg),
                    border = BorderStroke(1.dp, BadgeReviewAmberBorder)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BadgeReviewAmberText.copy(alpha = 0.15f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = BadgeReviewAmberText,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Repayment Discrepancy",
                                style = MaterialTheme.typography.titleSmall,
                                color = BadgeReviewAmberText,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = data.discrepancyMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextPrimary,
                                fontSize = 12.sp,
                                lineHeight = 17.sp
                            )
                        }
                    }
                }
            }

            // Missing Fields Warning (if any)
            if (data.missingFields.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = IconBoxRedBg.copy(alpha = 0.3f)),
                    border = BorderStroke(1.dp, IconBoxRedTint.copy(alpha = 0.3f))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Uncertain / Missing Terms",
                            style = MaterialTheme.typography.titleSmall,
                            color = IconBoxRedTint,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "The following fields could not be extracted with high confidence: ${data.missingFields.joinToString(", ")}. You can verify or enter them manually in the calculator.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextPrimary,
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Key Clauses Extracted Card
            if (data.extractedClauses.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    border = BorderStroke(1.dp, BorderNeutral)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Text(
                            text = "Extracted Fee Clauses",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        data.extractedClauses.forEachIndexed { index, clause ->
                            if (index > 0) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(BorderNeutral.copy(alpha = 0.5f)))
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            ClauseItem(
                                title = clause.first,
                                detail = clause.second
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Primary Bottom Action Button
            PrimaryFinShieldButton(
                text = "Verify Repayment in Calculator",
                onClick = { onNavigateTo(Screen.LoanCalculator.route) },
                showArrow = true
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SnapshotDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = TextPrimary
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = valueColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
private fun ClauseItem(
    title: String,
    detail: String
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = detail,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            fontSize = 12.sp
        )
    }
}
