package com.shadcn.ui.themes

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

interface ShadcnStyles {
    val background: Color
    val foreground: Color
    val card: Color
    val cardForeground: Color
    val popover: Color
    val popoverForeground: Color
    val primary: Color
    val primaryForeground: Color
    val secondary: Color
    val secondaryForeground: Color
    val muted: Color
    val mutedForeground: Color
    val accent: Color
    val accentForeground: Color
    val destructive: Color
    val destructiveForeground: Color
    val border: Color
    val input: Color
    val ring: Color
    val chart1: Color
    val chart2: Color
    val chart3: Color
    val chart4: Color
    val chart5: Color
    val sidebar: Color
    val sidebarForeground: Color
    val sidebarPrimary: Color
    val sidebarPrimaryForeground: Color
    val sidebarAccent: Color
    val sidebarAccentForeground: Color
    val sidebarBorder: Color
    val sidebarRing: Color
    val snackbar: Color
    @Composable
    fun shadow2xs(): List<BoxShadow>
    @Composable
    fun shadowXs(): List<BoxShadow>
    @Composable
    fun shadowSm(): List<BoxShadow>
    @Composable
    fun shadow(): List<BoxShadow>
    @Composable
    fun shadowMd(): List<BoxShadow>
    @Composable
    fun shadowLg(): List<BoxShadow>
    @Composable
    fun shadowXl(): List<BoxShadow>
    @Composable
    fun shadow2xl(): List<BoxShadow>
}
