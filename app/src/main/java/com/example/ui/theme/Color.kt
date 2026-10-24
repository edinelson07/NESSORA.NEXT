package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// NEXORA Official Burgundy, Deep Black, Lilac & Magenta Palette
val NexoraBackground = Color(0xFF080307)
val NexoraSurfaceDark = Color(0xFF0D040D)
val NexoraSurface = Color(0xFF140813)
val NexoraSurfaceVariant = Color(0xFF1A0A19)
val NexoraSurfaceCard = Color(0xFF1C0D20)
val NexoraCardElevated = Color(0xFF26122B)
val NexoraCardHighlight = Color(0xFF33183B)
val NexoraSurfaceBorder = Color(0xFF47204E)
val NexoraSurfaceBorderGlow = Color(0xFF7A2E8A)

// Deep Burgundy & Wine
val NexoraWineDark = Color(0xFF5B1024)
val NexoraWineGlow = Color(0x405B1024)

// Vibrant Accents (Lilac Neon, Electric Purple & Magenta)
val NexoraLilacNeon = Color(0xFFC77DFF)
val NexoraPurpleElectric = Color(0xFF922BFF)
val NexoraMagenta = Color(0xFFF044C8)

// Secondary Compatibility Accents
val NexoraNeonGreen = Color(0xFF36E58D)
val NexoraCyanBlue = Color(0xFF27B8FF)

// Glow & Transparency Accents
val NexoraLilacGlow = Color(0x33C77DFF)
val NexoraPurpleGlow = Color(0x33922BFF)
val NexoraMagentaGlow = Color(0x33F044C8)

// Text & Status
val NexoraTextPrimary = Color(0xFFFFFFFF)
val NexoraTextSecondary = Color(0xFFB9A8BE)
val NexoraTextMuted = Color(0xFF7D6C82)

val NexoraError = Color(0xFFFF5252)
val NexoraWarning = Color(0xFFFFB020)
val NexoraSuccess = Color(0xFF36E58D)

// Brand Gradients
val NexoraBrandGradient = Brush.horizontalGradient(
    colors = listOf(NexoraLilacNeon, NexoraPurpleElectric, NexoraMagenta)
)

val NexoraCardGradient = Brush.verticalGradient(
    colors = listOf(NexoraSurfaceCard, NexoraSurfaceVariant)
)

val NexoraProGradient = Brush.linearGradient(
    colors = listOf(NexoraWineDark, NexoraCardElevated, NexoraSurfaceCard)
)

val NexoraHeroGradient = Brush.radialGradient(
    colors = listOf(Color(0xFF3D0E29), NexoraSurfaceCard, NexoraBackground),
    radius = 1200f
)

val NexoraNeonGlow = NexoraLilacGlow
val NexoraCyanGlow = NexoraMagentaGlow
val NexoraPurple = NexoraPurpleElectric
