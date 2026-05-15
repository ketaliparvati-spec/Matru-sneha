package com.example.matrusneha.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val MatruSnehaColorScheme = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    tertiary = TertiaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onSecondary = TextPrimary, // Better contrast on Peach
    onBackground = TextPrimary,
    onSurface = TextPrimary,
    error = ErrorColor
)

@Composable
fun MatruSnehaTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MatruSnehaColorScheme,
        // Optional: define a custom typography here if needed later
        content = content
    )
}
