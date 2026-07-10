package com.komoui.themes

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

internal val DefaultTypography: Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
)

/**
 * KomoUI text tokens, mirroring shadcn's Tailwind text scale (xs/sm/base/lg) with the weights the
 * components actually use. Access via `MaterialTheme.komoTypography` and prefer these over inline
 * `fontSize`/`FontWeight` so text stays consistent and themeable.
 *
 * @property titleLarge 18sp SemiBold — section / dialog headings.
 * @property title 16sp SemiBold — card and header titles.
 * @property titleMedium 16sp Medium — calendar/accordion headers and nav labels.
 * @property titleEmphasis 16sp Bold — emphasized titles (e.g. Sonner).
 * @property body 14sp Normal — default body text and form fields.
 * @property bodyMedium 14sp Medium — menu items, tabs, buttons.
 * @property label 12sp Normal — captions, footers, helper text.
 * @property labelMedium 12sp Medium — badges and small emphasized labels.
 */
data class KomoTypography(
    val titleLarge: TextStyle = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.SemiBold),
    val title: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.SemiBold),
    val titleMedium: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Medium),
    val titleEmphasis: TextStyle = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Bold),
    val body: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Normal),
    val bodyMedium: TextStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium),
    val label: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Normal),
    val labelMedium: TextStyle = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Medium),
)
