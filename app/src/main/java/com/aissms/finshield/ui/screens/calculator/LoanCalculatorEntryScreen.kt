package com.aissms.finshield.ui.screens.calculator

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.aissms.finshield.model.CalculatorState
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
fun LoanCalculatorEntryScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var principal by remember { mutableDoubleStateOf(500000.0) }
    var interestRate by remember { mutableDoubleStateOf(10.5) }
    var tenureMonths by remember { mutableIntStateOf(36) }

    val calcState = CalculatorState(principal, interestRate, tenureMonths)
    val currencyFormatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Loan EMI Calculator",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.LoanCalculator.route,
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
                message = "Live EMI Calculation Engine — Adjust parameters to view total repayment breakdown"
            )

            // Result Display Card
            FinShieldCard(
                backgroundColor = PrimaryNavy,
                borderColor = PrimaryNavy
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Monthly EMI",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = currencyFormatter.format(calcState.monthlyEmi),
                        style = MaterialTheme.typography.headlineLarge,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Principal Amount",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = currencyFormatter.format(calcState.principalAmount),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Total Interest",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                        Text(
                            text = currencyFormatter.format(calcState.totalInterest),
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Total Payable Amount:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White
                        )
                        Text(
                            text = currencyFormatter.format(calcState.totalPayable),
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Input Sliders Section
            Text(
                text = "Loan Parameters",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )

            // Principal Slider
            FinShieldCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Loan Amount", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = currencyFormatter.format(principal),
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = principal.toFloat(),
                    onValueChange = { principal = it.toDouble() },
                    valueRange = 50000f..2000000f,
                    steps = 39,
                    colors = SliderDefaults.colors(thumbColor = EmeraldGreen, activeTrackColor = EmeraldGreen)
                )
            }

            // Interest Rate Slider
            FinShieldCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Interest Rate (p.a.)", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = String.format(Locale.US, "%.1f %%", interestRate),
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = interestRate.toFloat(),
                    onValueChange = { interestRate = it.toDouble() },
                    valueRange = 5.0f..24.0f,
                    steps = 38,
                    colors = SliderDefaults.colors(thumbColor = EmeraldGreen, activeTrackColor = EmeraldGreen)
                )
            }

            // Tenure Slider
            FinShieldCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Loan Tenure", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                    Text(
                        text = "$tenureMonths Months (${tenureMonths / 12} Yrs)",
                        style = MaterialTheme.typography.titleMedium,
                        color = EmeraldGreen,
                        fontWeight = FontWeight.Bold
                    )
                }
                Slider(
                    value = tenureMonths.toFloat(),
                    onValueChange = { tenureMonths = it.toInt() },
                    valueRange = 12f..120f,
                    steps = 8,
                    colors = SliderDefaults.colors(thumbColor = EmeraldGreen, activeTrackColor = EmeraldGreen)
                )
            }
        }
    }
}
