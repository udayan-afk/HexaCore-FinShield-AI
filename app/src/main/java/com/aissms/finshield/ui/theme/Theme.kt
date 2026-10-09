package com.aissms.finshield.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val FinShieldLightColorScheme = lightColorScheme(
    primary = EmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = EmeraldGreenLight,
    onPrimaryContainer = EmeraldGreenDark,
    secondary = PrimaryNavy,
    onSecondary = Color.White,
    secondaryContainer = PrimaryNavyLight,
    onSecondaryContainer = Color.White,
    background = BackgroundLight,
    onBackground = TextPrimary,
    surface = SurfaceCard,
    onSurface = TextPrimary,
    surfaceVariant = BackgroundLight,
    onSurfaceVariant = TextSecondary,
    outline = BorderNeutral,
    error = RiskHighRed,
    onError = Color.White
)

@Composable
fun FinShieldTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // For FinShield brand consistency, we utilize our tailored light finance theme
    MaterialTheme(
        colorScheme = FinShieldLightColorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
