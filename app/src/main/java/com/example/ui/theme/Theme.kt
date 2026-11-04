package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CyberpunkDarkColorScheme = darkColorScheme(
    primary = EmpireGold,
    onPrimary = ObsidianBlack,
    primaryContainer = ObsidianCardElevated,
    onPrimaryContainer = EmpireGoldLight,
    secondary = NeonCyan,
    onSecondary = ObsidianBlack,
    secondaryContainer = Color(0xFF003840),
    onSecondaryContainer = NeonCyan,
    tertiary = NeonCrimson,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF4A0018),
    onTertiaryContainer = Color(0xFFFFB2C9),
    background = ObsidianBlack,
    onBackground = TextPlatinum,
    surface = ObsidianDarkSurface,
    onSurface = TextPlatinum,
    surfaceVariant = ObsidianCard,
    onSurfaceVariant = TextMuted,
    outline = GlassBorder,
    outlineVariant = Color(0xFF233047)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = CyberpunkDarkColorScheme,
        typography = Typography,
        content = content
    )
}
