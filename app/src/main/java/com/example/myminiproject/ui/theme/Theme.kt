package com.example.myminiproject.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DhanSathiColorScheme = lightColorScheme(
    primary = Blue600,
    onPrimary = Color.White,
    primaryContainer = Blue100,
    onPrimaryContainer = Blue900,
    secondary = Green600,
    onSecondary = Color.White,
    secondaryContainer = Green100,
    onSecondaryContainer = GreenDark,
    tertiary = Purple600,
    onTertiary = Color.White,
    tertiaryContainer = Purple100,
    onTertiaryContainer = Purple600,
    error = Red500,
    onError = Color.White,
    errorContainer = Red100,
    onErrorContainer = Red500,
    background = Gray50,
    onBackground = Slate800,
    surface = Color.White,
    onSurface = Slate800,
    surfaceVariant = Gray100,
    onSurfaceVariant = Gray600,
    outline = Gray300,
    outlineVariant = Gray200
)

@Composable
fun MyminiprojectTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DhanSathiColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Blue900.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}