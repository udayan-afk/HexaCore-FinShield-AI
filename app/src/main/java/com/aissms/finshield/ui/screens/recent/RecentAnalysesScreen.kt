package com.aissms.finshield.ui.screens.recent

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.navigation.Screen
import com.aissms.finshield.ui.theme.BadgeReviewAmberBg
import com.aissms.finshield.ui.theme.BadgeReviewAmberText
import com.aissms.finshield.ui.theme.BorderNeutral
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.IconBoxGreenBg
import com.aissms.finshield.ui.theme.IconBoxGreenTint
import com.aissms.finshield.ui.theme.IconBoxRedBg
import com.aissms.finshield.ui.theme.IconBoxRedTint
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.SurfaceCard
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary

@Composable
fun RecentAnalysesScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Recent Analyses",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.RecentAnalyses.route,
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
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Your Document History",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            // Sample / Recent analyzed document card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateTo(Screen.LoanAnalysisReport.route) },
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, BorderNeutral)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = IconBoxRedBg.copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = null,
                                tint = IconBoxRedTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "HDFC_Personal_Loan.pdf",
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Analyzed Today · 1 Discrepancy Flagged",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BadgeReviewAmberBg
                    ) {
                        Text(
                            text = "Review",
                            color = BadgeReviewAmberText,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Start New Analysis Banner
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = Color.White.copy(alpha = 0.15f),
                        modifier = Modifier.size(48.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.History,
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Analyze a new agreement",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Upload any PDF loan document to get a transparent breakdown of charges and EMI terms.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrimaryFinShieldButton(
                        text = "Upload PDF",
                        onClick = { onNavigateTo(Screen.LoanAnalysisUpload.route) }
                    )
                }
            }
        }
    }
}
