package com.shadcn.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.ShadcnStyles
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

enum class ButtonVariant {
    Default,
    Destructive,
    Outline,
    Secondary,
    Ghost,
    Link
}

enum class ButtonSize {
    Default,
    Sm,
    Lg,
    Icon
}

@Composable
internal fun getButtonColors(
    variant: ButtonVariant,
    isPressed: Boolean,
    shadcnStyles: ShadcnStyles
): ButtonColors {
    return when (variant) {
        ButtonVariant.Default -> {
            val containerColor = if (isPressed) shadcnStyles.primary.copy(alpha = 0.8f) else shadcnStyles.primary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnStyles.primaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnStyles.primary.copy(alpha = 0.5f),
                disabledContentColor = shadcnStyles.primaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Destructive -> {
            val containerColor = if (isPressed) shadcnStyles.destructive.copy(alpha = 0.8f) else shadcnStyles.destructive
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnStyles.destructiveForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnStyles.destructive.copy(alpha = 0.5f),
                disabledContentColor = shadcnStyles.destructiveForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Outline -> {
            val containerColor = if (isPressed) shadcnStyles.muted else shadcnStyles.background
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnStyles.foreground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.outlinedButtonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnStyles.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Secondary -> {
            val containerColor = if (isPressed) shadcnStyles.secondary.copy(alpha = 0.8f) else shadcnStyles.secondary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = shadcnStyles.secondaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = shadcnStyles.secondary.copy(alpha = 0.5f),
                disabledContentColor = shadcnStyles.secondaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Ghost -> {
            val containerColor = if (isPressed) shadcnStyles.accent else Color.Transparent
            val contentColor = if (isPressed) shadcnStyles.accentForeground else shadcnStyles.foreground
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnStyles.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Link -> {
            val contentColor = if (isPressed) shadcnStyles.primary.copy(alpha = 0.8f) else shadcnStyles.primary
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = shadcnStyles.primary.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * @param onClick: () -> Unit - Lambda function to be invoked when the button is clicked.
 * @param modifier: Modifier (optional, default: Modifier) - Modifier to be applied to the button.
 * @param variant: ButtonVariant (optional, default: ButtonVariant.Default) - The visual style of
 *      the button. See ButtonVariant enum for available options.
 * @param size: ButtonSize (optional, default: ButtonSize.Default) - The size of the button,
 *      affecting its padding and minimum height/width. See ButtonSize enum for available options.
 * @param enabled: Boolean (optional, default: true) - Controls the enabled state of the button.
 *      When false, the button will be visually disabled and will not respond to user input.
 * @param shape: Shape (optional, default: RoundedCornerShape(Radius.md)) - The shape of the
 *      button's container.
 * @param color: ButtonColors (optional, default: null) - Custom button colors. If not provided,
 * @param content: @Composable RowScope.() -> Unit - The content to be displayed inside the button.
 *      This is a composable lambda that has RowScope as its receiver, allowing for flexible
 *      layout of content within the button (e.g., text, icons).
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(MaterialTheme.radius.md),
    color: ButtonColors? = null,
    content: @Composable RowScope.() -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val styles = MaterialTheme.styles
    val isPressed = interactionSource.collectIsPressedAsState().value

    val buttonColors = getButtonColors(variant, isPressed, styles)

    val borderStroke = when (variant) {
        ButtonVariant.Outline -> BorderStroke(1.dp, styles.input)
        else -> null
    }

    val contentPadding = when (size) {
        ButtonSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ButtonSize.Sm -> PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ButtonSize.Lg -> PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ButtonSize.Icon -> PaddingValues(0.dp)
    }

    val minHeightModifier = when (size) {
        ButtonSize.Default -> Modifier.defaultMinSize(minHeight = 40.dp)
        ButtonSize.Sm -> Modifier.defaultMinSize(minHeight = 36.dp)
        ButtonSize.Lg -> Modifier.defaultMinSize(minHeight = 44.dp)
        ButtonSize.Icon -> Modifier
            .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
    }

    // Common text style for buttons
    val buttonTextStyle = TextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Medium
    )

    // For link variant, add underline
    val linkTextStyle = if (variant == ButtonVariant.Link) {
        buttonTextStyle.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
    } else {
        buttonTextStyle
    }

    // Use TextButton for Ghost and Link variants to match behavior and remove default elevation
    if (variant == ButtonVariant.Ghost || variant == ButtonVariant.Link) {
        TextButton(
            onClick = onClick,
            modifier = Modifier.then(minHeightModifier).then(modifier),
            enabled = enabled,
            shape = shape,
            colors = color ?: buttonColors,
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                content = content
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = Modifier.then(minHeightModifier).then(modifier),
            enabled = enabled,
            shape = shape,
            colors = color ?: buttonColors,
            border = borderStroke,
            contentPadding = contentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                content = content
            )
        }
    }
}

@Composable
private fun ButtonContent(
    content: @Composable RowScope.() -> Unit,
    textStyle: TextStyle
) {
    ProvideTextStyle(textStyle) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            content = content
        )
    }
}