package com.aissms.finshield.ui.screens.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.aissms.finshield.ui.components.FinShieldScreenTopBar
import com.aissms.finshield.ui.components.FinShieldTextField
import com.aissms.finshield.ui.components.MockNoticeBanner
import com.aissms.finshield.ui.components.OutlinedFinShieldButton
import com.aissms.finshield.ui.components.PrimaryFinShieldButton
import com.aissms.finshield.ui.theme.EmeraldGreen
import com.aissms.finshield.ui.theme.PrimaryNavy
import com.aissms.finshield.ui.theme.TextPrimary
import com.aissms.finshield.ui.theme.TextSecondary

@Composable
fun AuthScreen(
    onLoginSuccess: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var mobileOrEmail by remember { mutableStateOf("9876543210") }
    var passwordOrOtp by remember { mutableStateOf("123456") }
    var fullName by remember { mutableStateOf("Alex Sharma") }

    Scaffold(
        topBar = {
            FinShieldScreenTopBar(title = "Account Access")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                MockNoticeBanner(
                    message = "Demo Auth Mode: Click 'Log In' or 'Continue as Guest' to enter FinShield Dashboard."
                )

                Spacer(modifier = Modifier.height(20.dp))

                // App Branding Header
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PrimaryNavy,
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shield,
                            contentDescription = null,
                            tint = EmeraldGreen,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = if (selectedTab == 0) "Welcome Back" else "Create FinShield Account",
                            style = MaterialTheme.typography.headlineMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Protect your loans with AI transparency",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Auth Tabs
                TabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.White,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = EmeraldGreen
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { selectedTab = 0 },
                        text = {
                            Text(
                                "Log In",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selectedTab == 0) PrimaryNavy else TextSecondary,
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { selectedTab = 1 },
                        text = {
                            Text(
                                "Register",
                                style = MaterialTheme.typography.titleMedium,
                                color = if (selectedTab == 1) PrimaryNavy else TextSecondary,
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                if (selectedTab == 1) {
                    FinShieldTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = "Full Name",
                        placeholder = "Enter your full name",
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                }

                FinShieldTextField(
                    value = mobileOrEmail,
                    onValueChange = { mobileOrEmail = it },
                    label = "Mobile Number / Email",
                    placeholder = "e.g. +91 9876543210",
                    leadingIcon = Icons.Default.Phone,
                    keyboardType = KeyboardType.Phone
                )

                Spacer(modifier = Modifier.height(14.dp))

                FinShieldTextField(
                    value = passwordOrOtp,
                    onValueChange = { passwordOrOtp = it },
                    label = if (selectedTab == 0) "Password / OTP" else "Create Password",
                    placeholder = "••••••••",
                    leadingIcon = Icons.Default.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password
                )
            }

            Column {
                PrimaryFinShieldButton(
                    text = if (selectedTab == 0) "Log In (Demo)" else "Register & Continue",
                    onClick = onLoginSuccess
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedFinShieldButton(
                    text = "Continue as Guest User",
                    onClick = onLoginSuccess
                )
            }
        }
    }
}
