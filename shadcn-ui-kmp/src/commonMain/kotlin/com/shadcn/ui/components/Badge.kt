package com.shadcn.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

enum class BadgeVariant {
    Default,
    Secondary,
    Destructive,
    Outline
}

/**
 * Displays a small, informative label with customizable styling based on variants.
 *
 * This badge component allows for different visual appearances (variants) such as Default,
 * Secondary, Destructive, and Outline. It can have a custom background color and corner radius.
 * The content of the badge is provided via a composable lambda.
 *
 * @param modifier Optional [Modifier] for this Badge.
 * @param variant The visual style of the badge. Determines default colors and border if not overridden.
 *   See [BadgeVariant] for available options. Defaults to [BadgeVariant.Default].
 * @param backgroundColor Optional explicit background color for the badge. If null, the color is
 *   determined by the selected [variant] and the current theme.
 * @param roundedSize The corner radius for the badge's shape. Defaults to a fully rounded shape
 *   (e.g., `Radius.full` which might correspond to `CircleShape` or a large Dp value).
 * @param content A composable lambda defining the content to be displayed inside the badge.
 *   Typically this will be a [androidx.compose.material3.Text] composable.
 */
@Composable
fun Badge(
    modifier: Modifier = Modifier,
    variant: BadgeVariant = BadgeVariant.Default,
    backgroundColor: Color? = null,
    roundedSize: Dp = MaterialTheme.radius.full,
    content: (@Composable () -> Unit)? = null
) {
    val styles = MaterialTheme.styles

    val containerColor = backgroundColor ?: when (variant) {
        BadgeVariant.Default -> styles.primary
        BadgeVariant.Secondary -> styles.secondary
        BadgeVariant.Destructive -> styles.destructive
        BadgeVariant.Outline -> styles.background
    }

    val contentColor = when (variant) {
        BadgeVariant.Default -> styles.primaryForeground
        BadgeVariant.Secondary -> styles.secondaryForeground
        BadgeVariant.Destructive -> styles.destructiveForeground
        BadgeVariant.Outline -> styles.foreground
    }

    val borderStroke = when (variant) {
        BadgeVariant.Outline -> BorderStroke(1.dp, styles.input)
        else -> null
    }

    val size = if (content != null) 16.dp else 6.dp

    val shape = if (content != null) {
        RoundedCornerShape(roundedSize)
    } else {
        CircleShape
    }

    val borderShape = if (content != null) {
        RoundedCornerShape(roundedSize)
    } else {
        CircleShape
    }

    Box(
        modifier = modifier
            .defaultMinSize(minWidth = size, minHeight = size)
            .background(containerColor, borderShape)
            .then(borderStroke?.let { Modifier.border(it, shape) }
                ?: Modifier) // Apply border if it exists
            .then(
                if (content != null)
                    Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        if (content != null) {
            ProvideTextStyle(
                value = TextStyle(
                    color = contentColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp
                )
            ) {
                content()
            }
        }
    }
}