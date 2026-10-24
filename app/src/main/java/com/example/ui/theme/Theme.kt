package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NexoraDarkColorScheme = darkColorScheme(
    primary = NexoraLilacNeon,
    onPrimary = NexoraBackground,
    primaryContainer = NexoraCardElevated,
    onPrimaryContainer = NexoraLilacNeon,
    secondary = NexoraMagenta,
    onSecondary = NexoraBackground,
    secondaryContainer = NexoraWineDark,
    onSecondaryContainer = NexoraLilacNeon,
    tertiary = NexoraPurpleElectric,
    onTertiary = NexoraBackground,
    tertiaryContainer = NexoraSurfaceCard,
    onTertiaryContainer = NexoraLilacNeon,
    background = NexoraBackground,
    onBackground = NexoraTextPrimary,
    surface = NexoraSurface,
    onSurface = NexoraTextPrimary,
    surfaceVariant = NexoraSurfaceVariant,
    onSurfaceVariant = NexoraTextSecondary,
    outline = NexoraSurfaceBorder,
    error = NexoraError,
    onError = NexoraBackground
)

@Composable
fun NexoraTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NexoraDarkColorScheme,
        typography = Typography,
        content = content
    )
}

// Backward-compatible alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    NexoraTheme(content = content)
}
