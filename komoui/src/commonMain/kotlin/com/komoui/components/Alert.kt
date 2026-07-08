package com.komoui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.radius
import com.komoui.themes.styles

enum class AlertVariant {
    Default, Destructive
}

/**
 * A short, important message.
 *
 * @param modifier The modifier to be applied to the alert container.
 * @param variant The visual style of the alert (Default or Destructive).
 * @param colors The [AlertStyle] resolving the alert's colors. Defaults to
 *   [AlertDefaults.colors] for [variant]; a caller-supplied value overrides the variant defaults.
 * @param icon Optional icon to display at the start of the alert.
 * @param title The composable content for the alert's title.
 * @param description The composable content for the alert's description.
 */
@Composable
fun Alert(
    modifier: Modifier = Modifier,
    variant: AlertVariant = AlertVariant.Default,
    colors: AlertStyle = AlertDefaults.colors(variant),
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit
) {
    val radius = MaterialTheme.radius
    // Variant defaults live in AlertDefaults.colors(variant); a caller-supplied AlertStyle wins.
    val titleColor = colors.titleColor
    val descriptionColor = colors.descriptionColor

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colors.backgroundColor, RoundedCornerShape(radius.md))
            .border(BorderStroke(1.dp, colors.borderColors), RoundedCornerShape(radius.md))
            .padding(16.dp)
    ) {
        icon?.let {
            // Icon size and padding
            Column(modifier = Modifier.padding(end = 12.dp)) {
                ProvideTextStyle(value = TextStyle(color = titleColor)) {
                    icon()
                }
            }
        }

        Column {
            ProvideTextStyle(
                value = TextStyle(
                    color = titleColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
                )
            ) {
                title()
            }
            Spacer(modifier = Modifier.height(4.dp))
            ProvideTextStyle(
                value = TextStyle(
                    color = descriptionColor,
                    fontSize = 14.sp,
                )
            ) {
                description()
            }
        }
    }
}

data class AlertStyle(
    val borderColors: Color,
    val backgroundColor: Color,
    val titleColor: Color,
    val descriptionColor: Color
)

/** Default [AlertStyle]s for [Alert], per [AlertVariant]. */
object AlertDefaults {
    /** Theme-derived colors for the given [variant]. Caller overrides take precedence. */
    @Composable
    fun colors(variant: AlertVariant = AlertVariant.Default): AlertStyle {
        val styles = MaterialTheme.styles
        return when (variant) {
            AlertVariant.Default -> AlertStyle(
                borderColors = styles.border,
                backgroundColor = styles.background,
                titleColor = styles.foreground,
                descriptionColor = styles.mutedForeground
            )
            AlertVariant.Destructive -> AlertStyle(
                borderColors = styles.destructive,
                backgroundColor = styles.background,
                titleColor = styles.destructive,
                descriptionColor = styles.destructive.copy(alpha = 0.8f)
            )
        }
    }
}