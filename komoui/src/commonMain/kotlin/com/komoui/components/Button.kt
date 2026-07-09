package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.minimumInteractiveComponentSize
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
import com.komoui.themes.KomoStyles
import com.komoui.themes.radius
import com.komoui.themes.styles

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
    Xs,
    Lg,
    Icon,
    IconSm
}

@Composable
internal fun getButtonColors(
    variant: ButtonVariant,
    isPressed: Boolean,
    komoStyles: KomoStyles
): ButtonColors {
    return when (variant) {
        ButtonVariant.Default -> {
            val containerColor = if (isPressed) komoStyles.primary.copy(alpha = 0.8f) else komoStyles.primary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = komoStyles.primaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = komoStyles.primary.copy(alpha = 0.5f),
                disabledContentColor = komoStyles.primaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Destructive -> {
            val containerColor = if (isPressed) komoStyles.destructive.copy(alpha = 0.8f) else komoStyles.destructive
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = komoStyles.destructiveForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = komoStyles.destructive.copy(alpha = 0.5f),
                disabledContentColor = komoStyles.destructiveForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Outline -> {
            val containerColor = if (isPressed) komoStyles.muted else komoStyles.background
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = komoStyles.foreground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.outlinedButtonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = komoStyles.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Secondary -> {
            val containerColor = if (isPressed) komoStyles.secondary.copy(alpha = 0.8f) else komoStyles.secondary
            val animatedContainerColor = animateColorAsState(
                targetValue = containerColor,
                animationSpec = tween(durationMillis = 100), label = "containerColorAnimation"
            )
            val animatedContentColor = animateColorAsState(
                targetValue = komoStyles.secondaryForeground,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.buttonColors(
                containerColor = animatedContainerColor.value,
                contentColor = animatedContentColor.value,
                disabledContainerColor = komoStyles.secondary.copy(alpha = 0.5f),
                disabledContentColor = komoStyles.secondaryForeground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Ghost -> {
            val containerColor = if (isPressed) komoStyles.accent else Color.Transparent
            val contentColor = if (isPressed) komoStyles.accentForeground else komoStyles.foreground
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = containerColor,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = komoStyles.foreground.copy(alpha = 0.5f)
            )
        }
        ButtonVariant.Link -> {
            val contentColor = if (isPressed) komoStyles.primary.copy(alpha = 0.8f) else komoStyles.primary
            val animatedContentColor = animateColorAsState(
                targetValue = contentColor,
                animationSpec = tween(durationMillis = 100), label = "contentColorAnimation"
            )
            ButtonDefaults.textButtonColors(
                containerColor = Color.Transparent,
                contentColor = animatedContentColor.value,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = komoStyles.primary.copy(alpha = 0.5f)
            )
        }
    }
}

/**
 * A composable button component for KomoUI.
 *
 * @param onClick Lambda function to be invoked when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param variant The visual style of the button. See [ButtonVariant] for available options.
 * @param size The size of the button, affecting its padding and minimum height/width.
 *      See [ButtonSize] for available options.
 * @param enabled Controls the enabled state of the button.
 *      When false, the button will be visually disabled and will not respond to user input.
 * @param loading When true, the button becomes non-interactive and shows a loading spinner
 *      before the content.
 * @param fullWidth When true, the button fills the maximum available width.
 * @param shape The shape of the button's container.
 * @param contentPadding Custom content padding. When non-null, overrides the size-derived padding.
 * @param color Custom button colors. If not provided, colors are derived from the variant.
 * @param content The content to be displayed inside the button.
 */
@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Default,
    size: ButtonSize = ButtonSize.Default,
    enabled: Boolean = true,
    loading: Boolean = false,
    fullWidth: Boolean = false,
    shape: Shape = RoundedCornerShape(MaterialTheme.radius.md),
    contentPadding: PaddingValues? = null,
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

    val resolvedContentPadding = contentPadding ?: when (size) {
        ButtonSize.Default -> PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ButtonSize.Sm -> PaddingValues(horizontal = 12.dp, vertical = 6.dp)
        ButtonSize.Xs -> PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ButtonSize.Lg -> PaddingValues(horizontal = 32.dp, vertical = 12.dp)
        ButtonSize.Icon -> PaddingValues(0.dp)
        ButtonSize.IconSm -> PaddingValues(0.dp)
    }

    val minHeightModifier = when (size) {
        ButtonSize.Default -> Modifier.defaultMinSize(minHeight = 40.dp)
        ButtonSize.Sm -> Modifier.defaultMinSize(minHeight = 36.dp)
        ButtonSize.Xs -> Modifier.defaultMinSize(minHeight = 32.dp)
        ButtonSize.Lg -> Modifier.defaultMinSize(minHeight = 44.dp)
        ButtonSize.Icon -> Modifier
            .defaultMinSize(minWidth = 36.dp, minHeight = 36.dp)
        ButtonSize.IconSm -> Modifier
            .defaultMinSize(minWidth = 28.dp, minHeight = 28.dp)
    }

    // Common text style for buttons
    val buttonTextStyle = TextStyle(
        fontSize = if (size == ButtonSize.Xs) 12.sp else 14.sp,
        fontWeight = FontWeight.Medium
    )

    // For link variant, add underline
    val linkTextStyle = if (variant == ButtonVariant.Link) {
        buttonTextStyle.copy(textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline)
    } else {
        buttonTextStyle
    }

    val isEnabled = enabled && !loading

    // minimumInteractiveComponentSize reserves a 48.dp touch target (visual stays small);
    // must precede the defaultMinSize size modifier. Covers the sub-48 sizes (Xs 32, IconSm 28).
    val baseModifier = if (fullWidth) {
        modifier.minimumInteractiveComponentSize().then(minHeightModifier).fillMaxWidth()
    } else {
        modifier.minimumInteractiveComponentSize().then(minHeightModifier)
    }

    // Use TextButton for Ghost and Link variants to match behavior and remove default elevation
    if (variant == ButtonVariant.Ghost || variant == ButtonVariant.Link) {
        TextButton(
            onClick = onClick,
            modifier = baseModifier,
            enabled = isEnabled,
            shape = shape,
            colors = color ?: buttonColors,
            contentPadding = resolvedContentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                loading = loading,
                content = content
            )
        }
    } else {
        Button(
            onClick = onClick,
            modifier = baseModifier,
            enabled = isEnabled,
            shape = shape,
            colors = color ?: buttonColors,
            border = borderStroke,
            contentPadding = resolvedContentPadding,
            interactionSource = interactionSource
        ) {
            ButtonContent(
                textStyle = linkTextStyle,
                loading = loading,
                content = content
            )
        }
    }
}

@Composable
private fun ButtonContent(
    content: @Composable RowScope.() -> Unit,
    textStyle: TextStyle,
    loading: Boolean = false
) {
    ProvideTextStyle(textStyle) {
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    color = LocalContentColor.current,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
            }
            content()
        }
    }
}

/**
 * A convenience composable for icon-only buttons.
 * Thin wrapper around [Button] with defaults suited for icon buttons.
 *
 * @param onClick Lambda function to be invoked when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param variant The visual style of the button. Defaults to [ButtonVariant.Ghost].
 * @param size The size of the button. Defaults to [ButtonSize.Icon].
 * @param enabled Controls the enabled state of the button.
 * @param shape The shape of the button's container.
 * @param color Custom button colors.
 * @param content The icon content to be displayed inside the button.
 */
@Composable
fun IconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    variant: ButtonVariant = ButtonVariant.Ghost,
    size: ButtonSize = ButtonSize.Icon,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(MaterialTheme.radius.md),
    color: ButtonColors? = null,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        variant = variant,
        size = size,
        enabled = enabled,
        shape = shape,
        color = color,
        content = content
    )
}
