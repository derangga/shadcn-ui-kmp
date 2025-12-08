package com.shadcn.ui.themes

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf

internal val LocalShadcnStyles = staticCompositionLocalOf<ShadcnStyles> { LightStyles }
internal val LocalShadcnRadius = staticCompositionLocalOf<ShadcnRadius> { Radius }
internal val LocalShadcnDarkMode = staticCompositionLocalOf { false }


/**
 * Provides [ShadcnStyles] and [ShadcnRadius] through a [CompositionLocalProvider] to be used in Shadcn Compose components.
 * It also applies MaterialTheme with the provided or default Material colors and typography.
 * notes:
 * - Use MaterialTheme.colorScheme for Material Design components.
 * - Use MaterialTheme.shadcnColors for ShadCN-specific styling.
 * - Use MaterialTheme.radius for ShadCN-specific styling.
 *
 * @param isDarkTheme Whether the theme should be dark or light. Defaults to the system setting.
 * @param shadcnLightColors The [ShadcnStyles] to be used for the light theme. Defaults to [LightStyles].
 * @param shadcnDarkColors The [ShadcnStyles] to be used for the dark theme. Defaults to [DarkStyles].
 * @param materialLightColors The Material 3 [ColorScheme] to be used for the light theme. Defaults to [lightColorScheme].
 * @param materialDarkColors The Material 3 [ColorScheme] to be used for the dark theme. Defaults to [darkColorScheme].
 * @param shadcnRadius The [ShadcnRadius] to be used. Defaults to [Radius].
 * @param typography The Material 3 [Typography] to be used. Defaults to [DefaultTypography].
 * @param content The composable content to be themed.
 */
@Composable
fun ShadcnTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    shadcnLightColors: ShadcnStyles = LightStyles,
    shadcnDarkColors: ShadcnStyles = DarkStyles,
    materialLightColors: ColorScheme = lightColorScheme(),
    materialDarkColors: ColorScheme = darkColorScheme(),
    shadcnRadius: ShadcnRadius = Radius,
    typography: Typography? = null,
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkTheme) shadcnDarkColors else shadcnLightColors
    val materialColorScheme = if (isDarkTheme) materialDarkColors else materialLightColors
    CompositionLocalProvider(
        LocalShadcnStyles provides colors,
        LocalShadcnRadius provides shadcnRadius,
        LocalShadcnDarkMode provides isDarkTheme
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = typography ?: DefaultTypography,
            content = content
        )
    }
}

val MaterialTheme.styles: ShadcnStyles
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnStyles.current

val MaterialTheme.radius: ShadcnRadius
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnRadius.current

val MaterialTheme.isDark: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalShadcnDarkMode.current