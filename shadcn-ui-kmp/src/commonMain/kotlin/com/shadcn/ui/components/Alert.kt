package com.shadcn.ui.components

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
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

enum class AlertVariant {
    Default, Destructive
}

/**
 * Displays a short, important message to the user.
 *
 * @param modifier The modifier to be applied to the alert container.
 * @param variant The visual style of the alert (Default or Destructive).
 * @param colors that will be used to resolve the colors used for this alert in
 *   different states. See [AlertDefaults.colors].
 * @param icon Optional icon to display at the start of the alert.
 * @param title The composable content for the alert's title.
 * @param description The composable content for the alert's description.
 */
@Composable
fun Alert(
    modifier: Modifier = Modifier,
    variant: AlertVariant = AlertVariant.Default,
    colors: AlertStyle = AlertDefaults.colors(),
    icon: (@Composable () -> Unit)? = null,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit
) {
    val radius = MaterialTheme.radius
    val titleColor = when (variant) {
        AlertVariant.Default -> colors.titleColor
        AlertVariant.Destructive -> MaterialTheme.styles.destructive
    }

    val descriptionColor = when (variant) {
        AlertVariant.Default -> colors.descriptionColor
        AlertVariant.Destructive -> MaterialTheme.styles.destructive.copy(alpha = 0.8f)
    }

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

object AlertDefaults {
    @Composable
    fun colors(): AlertStyle {
        val styles = MaterialTheme.styles
        return AlertStyle(
            borderColors = styles.border,
            backgroundColor = styles.background,
            titleColor = styles.foreground,
            descriptionColor = styles.mutedForeground
        )
    }
}