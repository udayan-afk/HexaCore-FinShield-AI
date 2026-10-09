package com.aissms.finshield.ui.screens.profile

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.aissms.finshield.ui.components.FinShieldBottomBar
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.navigation.Screen
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
fun ProfileScreen(
    onNavigateTo: (String) -> Unit,
    onBackClick: () -> Unit
) {
    Scaffold(
        topBar = {
            FinShieldScreenTopBar(
                title = "Profile & Settings",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            FinShieldBottomBar(
                currentRoute = Screen.Profile.route,
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
            // Profile Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = PrimaryNavy)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = EmeraldGreen,
                        modifier = Modifier.size(54.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "Guest User",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color.White.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = "FinShield Standard Plan",
                                color = EmeraldGreen,
                                style = MaterialTheme.typography.labelSmall,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }
                    }
                }
            }

            // Preferences Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                border = BorderStroke(1.dp, BorderNeutral)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Preferences & App Settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    ProfileSettingItem(
                        icon = Icons.Default.Language,
                        title = "App Language",
                        subtitle = "English",
                        onClick = { onNavigateTo(Screen.LanguageSelection.route) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileSettingItem(
                        icon = Icons.Default.Shield,
                        title = "Data Privacy & Security",
                        subtitle = "Local PDF parsing & zero tracking",
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileSettingItem(
                        icon = Icons.Default.HelpOutline,
                        title = "Help & Support",
                        subtitle = "Learn how loan discrepancy flags work",
                        onClick = {}
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    ProfileSettingItem(
                        icon = Icons.AutoMirrored.Filled.ExitToApp,
                        title = "Sign Out / Switch Account",
                        subtitle = "Return to authentication screen",
                        iconTint = IconBoxRedTint,
                        iconBg = IconBoxRedBg.copy(alpha = 0.4f),
                        onClick = { onNavigateTo(Screen.Auth.route) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileSettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconTint: Color = IconBoxGreenTint,
    iconBg: Color = IconBoxGreenBg,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = iconBg,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextSecondary,
                fontSize = 12.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            contentDescription = null,
            tint = TextSecondary,
            modifier = Modifier.size(16.dp)
        )
    }
}
