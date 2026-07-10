package com.komoui.themes

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

internal val LocalKomoStyles = staticCompositionLocalOf<KomoStyles> { LightStyles }
internal val LocalKomoRadius = staticCompositionLocalOf<KomoRadius> { Radius }
internal val LocalKomoDarkMode = staticCompositionLocalOf { false }


/**
 * Provides [KomoStyles] and [KomoRadius] through a [CompositionLocalProvider] to be used in KomoUI components.
 * It also applies MaterialTheme with the provided or default Material colors and typography.
 * notes:
 * - Use MaterialTheme.colorScheme for Material Design components.
 * - Use MaterialTheme.styles for KomoUI-specific styling.
 * - Use MaterialTheme.radius for KomoUI-specific styling.
 *
 * @param isDarkTheme Whether the theme should be dark or light. Defaults to the system setting.
 * @param komoLightColors The [KomoStyles] to be used for the light theme. Defaults to [LightStyles].
 * @param komoDarkColors The [KomoStyles] to be used for the dark theme. Defaults to [DarkStyles].
 * @param materialLightColors The Material 3 [ColorScheme] to be used for the light theme. Defaults to [lightColorScheme].
 * @param materialDarkColors The Material 3 [ColorScheme] to be used for the dark theme. Defaults to [darkColorScheme].
 * @param komoRadius The [KomoRadius] to be used. Defaults to [Radius].
 * @param strings The [KomoStrings] (localizable labels/descriptions) to be used. Defaults to English.
 * @param typography The Material 3 [Typography] to be used. Defaults to [DefaultTypography].
 * @param content The composable content to be themed.
 */
@Composable
fun KomoTheme(
    isDarkTheme: Boolean = isSystemInDarkTheme(),
    komoLightColors: KomoStyles = LightStyles,
    komoDarkColors: KomoStyles = DarkStyles,
    materialLightColors: ColorScheme = lightColorScheme(),
    materialDarkColors: ColorScheme = darkColorScheme(),
    komoRadius: KomoRadius = Radius,
    strings: KomoStrings = KomoStrings(),
    typography: Typography? = null,
    content: @Composable () -> Unit,
) {
    val colors = if (isDarkTheme) komoDarkColors else komoLightColors
    val materialColorScheme = if (isDarkTheme) materialDarkColors else materialLightColors
    CompositionLocalProvider(
        LocalKomoStyles provides colors,
        LocalKomoRadius provides komoRadius,
        LocalKomoStrings provides strings,
        LocalKomoDarkMode provides isDarkTheme
    ) {
        MaterialTheme(
            colorScheme = materialColorScheme,
            typography = typography ?: DefaultTypography,
            content = content
        )
    }
}

val MaterialTheme.styles: KomoStyles
    @Composable
    @ReadOnlyComposable
    get() = LocalKomoStyles.current

val MaterialTheme.radius: KomoRadius
    @Composable
    @ReadOnlyComposable
    get() = LocalKomoRadius.current

val MaterialTheme.isDark: Boolean
    @Composable
    @ReadOnlyComposable
    get() = LocalKomoDarkMode.current