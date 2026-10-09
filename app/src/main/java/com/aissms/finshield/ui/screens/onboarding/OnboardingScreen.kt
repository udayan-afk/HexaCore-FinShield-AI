package com.aissms.finshield.ui.screens.onboarding

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.ui.components.OutlinedFinShieldButton
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary

data class OnboardingPageData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val badge: String
)

val onboardingPages = listOf(
    OnboardingPageData(
        title = "AI Loan Agreement Scanner",
        description = "Upload or scan complex bank loan agreements. FinShield AI instantly flags hidden penalties, balloon payments, and predatory interest terms.",
        icon = Icons.Default.FindInPage,
        badge = "Document Intelligence"
    ),
    OnboardingPageData(
        title = "Real-Time Risk Scoring",
        description = "Get clear risk ratings (Low, Moderate, High) and plain-language summaries in English, Hindi, or Marathi before signing any contract.",
        icon = Icons.Default.Analytics,
        badge = "Risk Assessment"
    ),
    OnboardingPageData(
        title = "Smart Calculator & Loan Comparison",
        description = "Calculate true EMI costs and compare multiple bank offers side-by-side to save thousands in hidden charges.",
        icon = Icons.Default.Calculate,
        badge = "Financial Security"
    )
)

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit
) {
    var currentPage by remember { mutableIntStateOf(0) }
    val pageData = onboardingPages[currentPage]

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Top Bar with Skip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = PrimaryNavy.copy(alpha = 0.08f),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text(
                        text = pageData.badge,
                        color = PrimaryNavy,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                TextButton(onClick = onFinishOnboarding) {
                    Text(
                        text = "Skip",
                        style = MaterialTheme.typography.labelLarge,
                        color = TextSecondary
                    )
                }
            }

            // Main Illustration & Content
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    shape = CircleShape,
                    color = EmeraldGreen.copy(alpha = 0.12f),
                    modifier = Modifier.size(140.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = pageData.icon,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.size(70.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                Text(
                    text = pageData.title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TextPrimary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = pageData.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextSecondary,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            // Bottom Indicators & Action Buttons
            Column(modifier = Modifier.fillMaxWidth()) {
                // Page Indicators
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    onboardingPages.indices.forEach { index ->
                        val isSelected = index == currentPage
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .height(8.dp)
                                .width(if (isSelected) 24.dp else 8.dp)
                                .background(
                                    color = if (isSelected) EmeraldGreen else PrimaryNavy.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }

                if (currentPage < onboardingPages.size - 1) {
                    PrimaryFinShieldButton(
                        text = "Next",
                        onClick = { currentPage++ }
                    )
                } else {
                    PrimaryFinShieldButton(
                        text = "Get Started",
                        onClick = onFinishOnboarding
                    )
                }
            }
        }
    }
}
