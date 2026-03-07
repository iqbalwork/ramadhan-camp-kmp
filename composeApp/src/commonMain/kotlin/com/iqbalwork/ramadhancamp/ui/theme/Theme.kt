package com.iqbalwork.ramadhancamp.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue

/**
 * iqbalfauzi
 * Email: work.iqbalfauzi@gmail.com
 * Github: https://github.com/iqbalwork
 */
private val LightColors = lightColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryGreen,
    surface = LightSurface,
    background = LightBackground,
)

private val DarkColors = darkColorScheme(
    primary = PrimaryGreen,
    secondary = SecondaryGreen,
    surface = DarkSurface,
    background = DarkBackground,
)

@Composable
expect fun dynamicColorScheme(darkTheme: Boolean): ColorScheme?

@Composable
fun animateColorSchemeAsState(targetColorScheme: ColorScheme): ColorScheme {
    val primary by animateColorAsState(targetColorScheme.primary)
    val secondary by animateColorAsState(targetColorScheme.secondary)
    val surface by animateColorAsState(targetColorScheme.surface)
    val background by animateColorAsState(targetColorScheme.background)

    return targetColorScheme.copy(
        primary = primary,
        secondary = secondary,
        surface = surface,
        background = background,
    )
}

@Composable
fun RamadhanTheme(
    darkTheme: Boolean = false,
    useDynamicColor: Boolean = false,
    colorScheme: ColorScheme? = null,
    content: @Composable () -> Unit
) {

    val targetColorScheme = when {
        colorScheme != null -> colorScheme
        useDynamicColor -> dynamicColorScheme(darkTheme)
            ?: if (darkTheme) DarkColors else LightColors

        else -> if (darkTheme) DarkColors else LightColors
    }

    val animatedColorScheme = animateColorSchemeAsState(targetColorScheme)

    MaterialTheme(
        colorScheme = animatedColorScheme,
        typography = RamadhanTypography,
        content = content
    )
}
