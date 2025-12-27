// ui/theme/Theme.kt
package com.bilal.masterly.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    onPrimary = Color.Black,

    secondary = PurpleGrey80,
    onSecondary = Color.Black,

    tertiary = Pink80,
    onTertiary = Color.Black,

    background = Color(0xFF0F0F10),
    onBackground = Color(0xFFECECEC),

    surface = Color(0xFF141414),
    onSurface = Color(0xFFECECEC),

    surfaceVariant = Color(0xFF1C1C1E),
    onSurfaceVariant = Color(0xFFB4B4B4),

    outline = Color(0xFF3A3A3C),

    error = Color(0xFFCF6679),
    onError = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Purple40,
    onPrimary = Color.White,

    secondary = PurpleGrey40,
    onSecondary = Color.White,

    tertiary = Pink40,
    onTertiary = Color.White,

    background = Color(0xFFF6F6F7),
    onBackground = Color(0xFF1C1B1F),

    surface = Color.White,
    onSurface = Color(0xFF1C1B1F),

    surfaceVariant = Color(0xFFF0F2F5),
    onSurfaceVariant = Color(0xFF5F6368),

    outline = Color(0xFFD0D5DD),

    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun MasterlyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
