package com.github.snuffix.koleo

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val primary = Color(0xFF15155f)
val secondary = Color(0xFFcf3b6a)

@Composable
fun KoleoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        darkColorScheme(
            primary = primary,
            secondary = secondary,
            primaryContainer = primary,
            secondaryContainer = secondary
        )
    } else {
        lightColorScheme(
            primary = primary,
            secondary = secondary,
            primaryContainer = primary,
            secondaryContainer = secondary
        )
    }

    val shapes = Shapes(
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(8.dp)
    )

    MaterialTheme(
        colorScheme = colors,
        shapes = shapes,
        content = content
    )
}
