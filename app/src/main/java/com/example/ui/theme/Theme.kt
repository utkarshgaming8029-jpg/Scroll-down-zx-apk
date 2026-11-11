package com.example.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NexoraDarkColorScheme = darkColorScheme(
    primary = NexoraPurple,
    onPrimary = Color.White,
    primaryContainer = NexoraViolet,
    onPrimaryContainer = Color.White,
    secondary = NexoraNeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = NexoraElectricBlue.copy(alpha = 0.2f),
    onSecondaryContainer = NexoraNeonCyan,
    tertiary = NexoraPinkAccent,
    onTertiary = Color.White,
    background = NexoraDeepDark,
    onBackground = NexoraTextPrimary,
    surface = NexoraSurfaceDark,
    onSurface = NexoraTextPrimary,
    surfaceVariant = NexoraSurfaceLightDark,
    onSurfaceVariant = NexoraTextSecondary,
    outline = NexoraGlassBorder
)

private val NexoraLightColorScheme = lightColorScheme(
    primary = Color(0xFF7C3AED),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDDD6FE),
    onPrimaryContainer = Color(0xFF4C1D95),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBAE6FD),
    onSecondaryContainer = Color(0xFF0369A1),
    tertiary = NexoraPinkAccent,
    onTertiary = Color.White,
    background = Color(0xFF0B081E), // Premium dark-first experience
    onBackground = NexoraTextPrimary,
    surface = NexoraSurfaceDark,
    onSurface = NexoraTextPrimary,
    surfaceVariant = NexoraSurfaceLightDark,
    onSurfaceVariant = NexoraTextSecondary,
    outline = NexoraGlassBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to Nexora's signature Dark theme
    dynamicColor: Boolean = false, // Keep signature branding colors
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) NexoraDarkColorScheme else NexoraLightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                window.statusBarColor = NexoraDeepDark.toArgb()
                window.navigationBarColor = NexoraDeepDark.toArgb()
                WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
                WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
