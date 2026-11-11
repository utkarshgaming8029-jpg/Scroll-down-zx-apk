package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Nexora Signature Colors
val NexoraDeepDark = Color(0xFF080614)
val NexoraSurfaceDark = Color(0xFF120E29)
val NexoraSurfaceLightDark = Color(0xFF1B163B)
val NexoraGlassSurface = Color(0x33282054)
val NexoraGlassBorder = Color(0x33A78BFA)

// Brand Gradients & Accents
val NexoraPurple = Color(0xFF8B5CF6)
val NexoraViolet = Color(0xFF6D28D9)
val NexoraElectricBlue = Color(0xFF06B6D4)
val NexoraNeonCyan = Color(0xFF22D3EE)
val NexoraPinkAccent = Color(0xFFEC4899)
val NexoraTealAccent = Color(0xFF10B981)

val NexoraTextPrimary = Color(0xFFF8FAFC)
val NexoraTextSecondary = Color(0xFF94A3B8)
val NexoraTextMuted = Color(0xFF64748B)

val NexoraHeartRed = Color(0xFFEF4444)
val NexoraVerifiedBlue = Color(0xFF38BDF8)

// Gradients
val NexoraPrimaryGradient = Brush.linearGradient(
    colors = listOf(Color(0xFF8B5CF6), Color(0xFF06B6D4))
)

val NexoraAccentGradient = Brush.linearGradient(
    colors = listOf(Color(0xFFEC4899), Color(0xFF8B5CF6))
)

val NexoraStoryGradient = Brush.sweepGradient(
    colors = listOf(
        Color(0xFFF59E0B),
        Color(0xFFEC4899),
        Color(0xFF8B5CF6),
        Color(0xFF06B6D4),
        Color(0xFFF59E0B)
    )
)

val NexoraDarkCardGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0x551E1742),
        Color(0x33140F30)
    )
)
