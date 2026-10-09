package com.aissms.finshield.ui.screens.analyze

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileUpload
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aissms.finshield.model.MockLoanData
import com.aissms.finshield.model.RiskLevel
import com.aissms.finshield.model.SampleLoanContract
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldCard
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.MockNoticeBanner
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.components.RiskBadge
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.BorderNeutral
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.SurfaceCard
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary

@Composable
fun LoanAnalysisEntryScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var selectedContract by remember { mutableStateOf<SampleLoanContract?>(MockLoanData.sampleContracts.first()) }
    var contractInputText by remember { mutableStateOf(selectedContract?.sampleTextSnippet ?: "") }
    var isSimulatedScanActive by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Loan Agreement Analysis",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanAnalysis.route,
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
                message = "Phase 1 Preview: AI Analysis engine is simulated (Phase 2 Gemini model integration pending)."
            )

            // Input Method Selection Cards
            Text(
                text = "Select Document Source",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                DocumentSourceOption(
                    title = "Upload PDF",
                    icon = Icons.Default.FileUpload,
                    modifier = Modifier.weight(1f)
                )
                DocumentSourceOption(
                    title = "Scan Photo",
                    icon = Icons.Default.CameraAlt,
                    modifier = Modifier.weight(1f)
                )
                DocumentSourceOption(
                    title = "Paste Text",
                    icon = Icons.Default.ContentPaste,
                    modifier = Modifier.weight(1f),
                    isSelected = true
                )
            }

            // Quick Preset Samples for Hackathon Evaluators
            Text(
                text = "Or Test Sample Loan Templates",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            MockLoanData.sampleContracts.forEach { contract ->
                val isSelected = selectedContract?.id == contract.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedContract = contract
                            contractInputText = contract.sampleTextSnippet
                            isSimulatedScanActive = true
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) EmeraldGreen.copy(alpha = 0.08f) else SurfaceCard
                    ),
                    border = BorderStroke(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) EmeraldGreen else BorderNeutral
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = contract.title,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Text(
                                text = contract.bankName,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                        RiskBadge(riskLevel = contract.riskLevel)
                    }
                }
            }

            // Editable Loan Terms Input Field
            Text(
                text = "Loan Contract Text",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = contractInputText,
                onValueChange = { contractInputText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp),
                placeholder = { Text("Paste loan agreement clauses here...") },
                shape = RoundedCornerShape(12.dp)
            )

            PrimaryFinShieldButton(
                text = "Run FinShield AI Scan (Simulated)",
                onClick = { isSimulatedScanActive = true },
                icon = Icons.Default.Psychology
            )

            // Simulated AI Result Output Box
            if (isSimulatedScanActive && selectedContract != null) {
                FinShieldCard(
                    borderColor = EmeraldGreen
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Simulated Risk Summary",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        RiskBadge(riskLevel = selectedContract!!.riskLevel)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        color = PrimaryNavy.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Flagged Clause (${selectedContract!!.flagCount} Risk Indicators):",
                                style = MaterialTheme.typography.labelMedium,
                                color = PrimaryNavy,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = contractInputText,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextPrimary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "• Prepayment Penalty Warning: Penalty exceeds standard 2.0% threshold.\n• Interest Rate Adjustment: Floating rate subject to unannounced rate hikes.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
private fun DocumentSourceOption(
    title: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) EmeraldGreen.copy(alpha = 0.12f) else SurfaceCard,
        border = BorderStroke(
            width = if (isSelected) 2.dp else 1.dp,
            color = if (isSelected) EmeraldGreen else BorderNeutral
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) EmeraldGreen else PrimaryNavy,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}
