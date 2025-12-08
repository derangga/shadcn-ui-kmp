package com.shadcn.ui.themes

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object LightStyles : ShadcnStyles {
    override val background: Color = Color(0xFFFFFFFF)
    override val foreground: Color = Color(0xFF0A0A0A)
    override val card: Color = Color(0xFFFFFFFF)
    override val cardForeground: Color = Color(0xFF0A0A0A)
    override val popover: Color = Color(0xFFFFFFFF)
    override val popoverForeground: Color = Color(0xFF0A0A0A)
    override val primary: Color = Color(0xFF171717)
    override val primaryForeground: Color = Color(0xFFFAFAFA)
    override val secondary: Color = Color(0xFFF5F5F5)
    override val secondaryForeground: Color = Color(0xFF171717)
    override val muted: Color = Color(0xFFF5F5F5)
    override val mutedForeground: Color = Color(0xFF737373)
    override val accent: Color = Color(0xFFF5F5F5)
    override val accentForeground: Color = Color(0xFF171717)
    override val destructive: Color = Color(0xFFE7000B)
    override val destructiveForeground: Color = Color(0xFFFFFFFF)
    override val border: Color = Color(0xFFE5E5E5)
    override val input: Color = Color(0xFFE5E5E5)
    override val ring: Color = Color(0xFFA1A1A1)

    override val chart1: Color = Color(0xFFB2D4FF)
    override val chart2: Color = Color(0xFF3A81F6)
    override val chart3: Color = Color(0xFF2563EF)
    override val chart4: Color = Color(0xFF1A4EDA)
    override val chart5: Color = Color(0xFF1F3FAD)

    override val sidebar: Color = Color(0xFFFAFAFA)
    override val sidebarForeground: Color = Color(0xFF0A0A0A)
    override val sidebarPrimary: Color = Color(0xFF171717)
    override val sidebarPrimaryForeground: Color = Color(0xFFFAFAFA)
    override val sidebarAccent: Color = Color(0xFFF5F5F5)
    override val sidebarAccentForeground: Color = Color(0xFF171717)
    override val sidebarBorder: Color = Color(0xFFE5E5E5)
    override val sidebarRing: Color = Color(0xFFA1A1A1)
    override val snackbar: Color = Color(0xFFFFFFFF)

    @Composable
    override fun shadow2xs(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadowXs(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadowSm(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        ),
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 2.dp,
            spread = -1.dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadow(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        ),
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 2.dp,
            spread = (-1).dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadowMd(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        ),
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 2.dp,
            blurRadius = 4.dp,
            spread = (-1).dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadowLg(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        ),
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 4.dp,
            blurRadius = 6.dp,
            spread = -1.dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadowXl(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        ),
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 8.dp,
            blurRadius = 10.dp,
            spread = -1.dp,
            color = MaterialTheme.styles.border
        )
    )
    @Composable
    override fun shadow2xl(): List<BoxShadow> = listOf(
        BoxShadow(
            offsetX = 0.dp,
            offsetY = 1.dp,
            blurRadius = 3.dp,
            spread = 0.dp,
            color = MaterialTheme.styles.border
        )
    )
}