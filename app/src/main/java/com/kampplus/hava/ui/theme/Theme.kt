package com.kampplus.hava.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Sinema teması: her zaman koyu. Açılış ekranındaki gece perdesiyle aynı renkler.
 * Eşleşmeler: GO kartı = primaryContainer (yeşil), MAYBE = tertiaryContainer (amber),
 * SKIP = errorContainer (kırmızı).
 */
@Composable
fun HavaTheme(content: @Composable () -> Unit) {
    val colorScheme = darkColorScheme(
        primary = Color(0xFFFFD966),
        onPrimary = Color(0xFF12142B),
        primaryContainer = Color(0xFF1F4D3A),
        onPrimaryContainer = Color(0xFFBFF3D4),
        secondary = Color(0xFF8EC5F0),
        onSecondary = Color(0xFF12142B),
        secondaryContainer = Color(0xFF3A4380),
        onSecondaryContainer = Color(0xFFFFFFFF),
        tertiary = Color(0xFFFFC83D),
        onTertiary = Color(0xFF12142B),
        tertiaryContainer = Color(0xFF4A3B12),
        onTertiaryContainer = Color(0xFFFFE9A8),
        error = Color(0xFFFF8A80),
        onError = Color(0xFF12142B),
        errorContainer = Color(0xFF5C2229),
        onErrorContainer = Color(0xFFFFDAD6),
        background = Color(0xFF12142B),
        onBackground = Color(0xFFF4F1E8),
        surface = Color(0xFF12142B),
        onSurface = Color(0xFFF4F1E8),
        surfaceVariant = Color(0xFF252A52),
        onSurfaceVariant = Color(0xFF9AA0C8),
        surfaceContainerLowest = Color(0xFF0F1124),
        surfaceContainerLow = Color(0xFF171A35),
        surfaceContainer = Color(0xFF1B1F3B),
        surfaceContainerHigh = Color(0xFF20254A),
        surfaceContainerHighest = Color(0xFF252A52),
        outline = Color(0xFF4A5080),
        outlineVariant = Color(0xFF2F3566)
    )
    MaterialTheme(colorScheme = colorScheme, content = content)
}
