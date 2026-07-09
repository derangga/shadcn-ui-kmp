package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.radius
import com.komoui.themes.styles

data class InputOTPBorderStyle(
    val default: Color,
    val focus: Color,
    val error: Color,
)

data class InputOTPStyle(
    val background: Color,
    val text: Color,
    val disableText: Color,
    val activeRing: Color,
    val caret: Color,
    val border: InputOTPBorderStyle,
)

object InputOTPDefaults {
    val SlotSize: Dp = 40.dp
    val SlotGap: Dp = 0.dp
    val GroupGap: Dp = 8.dp

    @Composable
    fun colors(): InputOTPStyle {
        val styles = MaterialTheme.styles
        return InputOTPStyle(
            background = Color.Unspecified,
            text = styles.foreground,
            disableText = styles.mutedForeground,
            activeRing = styles.ring.copy(alpha = 0.5f),
            caret = styles.foreground,
            border = InputOTPBorderStyle(
                default = styles.input,
                focus = styles.ring,
                error = styles.destructive,
            )
        )
    }

    @Composable
    fun colors(overrides: InputOTPStyle.() -> InputOTPStyle): InputOTPStyle {
        return colors().overrides()
    }
}

/**
 * A Jetpack Compose Input OTP component for KomoUI.
 * Renders [length] character slots backed by a single hidden text field.
 *
 * @param value The current OTP value.
 * @param onValueChange Invoked when the OTP changes. Already trimmed to [length] and filtered.
 * @param modifier The modifier applied to the root row.
 * @param length Total number of slots.
 * @param enabled Whether input is enabled.
 * @param readOnly Whether the field displays its value but rejects edits.
 * @param focusRequester Optional [FocusRequester] so callers can focus the field programmatically.
 * @param isError Whether the component is in an error state.
 * @param keyboardType Soft-keyboard type. Defaults to [KeyboardType.NumberPassword].
 * @param keyboardActions Keyboard action callbacks (e.g. onDone).
 * @param separatorIndices Slot indices (0-based) AFTER which to render a separator. Empty for none.
 * @param separator Separator composable. Defaults to a small dash.
 * @param slotSize Width/height of each slot square.
 * @param colors Theme colors for the component.
 */
@Composable
fun InputOTP(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    length: Int = 6,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    focusRequester: FocusRequester? = null,
    isError: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.NumberPassword,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    separatorIndices: Set<Int> = emptySet(),
    separator: @Composable () -> Unit = { InputOTPSeparator() },
    slotSize: Dp = InputOTPDefaults.SlotSize,
    colors: InputOTPStyle = InputOTPDefaults.colors(),
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()
    // Numeric keyboard types only ever hold digits, applied to both display and input so an
    // external value with non-digits (or pasted whitespace/emoji) is filtered, not just truncated.
    val filterForKeyboard: (String) -> String = { raw ->
        when (keyboardType) {
            KeyboardType.Number, KeyboardType.NumberPassword, KeyboardType.Decimal ->
                raw.filter { it.isDigit() }
            else -> raw
        }
    }
    val sanitized = filterForKeyboard(value).take(length)

    BasicTextField(
        value = sanitized,
        onValueChange = { new ->
            val filtered = filterForKeyboard(new).take(length)
            if (filtered != sanitized) onValueChange(filtered)
        },
        modifier = modifier
            .minimumInteractiveComponentSize()
            .then(if (focusRequester != null) Modifier.focusRequester(focusRequester) else Modifier),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = LocalTextStyle.current.copy(color = Color.Transparent),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        keyboardActions = keyboardActions,
        singleLine = true,
        interactionSource = interactionSource,
        cursorBrush = SolidColor(Color.Transparent),
        decorationBox = { innerTextField ->
          Box {
            Box(modifier = Modifier.size(0.dp).alpha(0f)) { innerTextField() }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(if (enabled) 1f else 0.5f)
            ) {
                for (index in 0 until length) {
                    val isActive = isFocused && (index == sanitized.length ||
                            (sanitized.length == length && index == length - 1))
                    InputOTPSlot(
                        char = sanitized.getOrNull(index),
                        isActive = isActive,
                        isError = isError,
                        enabled = enabled,
                        size = slotSize,
                        colors = colors,
                    )
                    if (index in separatorIndices && index != length - 1) {
                        Spacer(modifier = Modifier.width(InputOTPDefaults.GroupGap))
                        separator()
                        Spacer(modifier = Modifier.width(InputOTPDefaults.GroupGap))
                    }
                }
            }
          }
        }
    )
}

@Composable
private fun InputOTPSlot(
    char: Char?,
    isActive: Boolean,
    isError: Boolean,
    enabled: Boolean,
    size: Dp,
    colors: InputOTPStyle,
) {
    val radius = MaterialTheme.radius
    val shape = RoundedCornerShape(radius.md)

    val borderColor by animateColorAsState(
        targetValue = when {
            isError -> colors.border.error
            isActive -> colors.border.focus
            else -> colors.border.default
        },
        animationSpec = tween(durationMillis = 150),
        label = "otpBorderColor"
    )

    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(colors.background, shape)
            .then(
                if (isActive) Modifier.border(3.dp, colors.activeRing, shape)
                else Modifier
            )
            .border(1.dp, borderColor, shape),
        contentAlignment = Alignment.Center,
    ) {
        if (char != null) {
            Text(
                text = char.toString(),
                style = TextStyle(
                    color = if (enabled) colors.text else colors.disableText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            )
        } else if (isActive) {
            BlinkingCaret(color = colors.caret)
        }
    }
}

@Composable
private fun BlinkingCaret(color: Color) {
    val transition = rememberInfiniteTransition(label = "otpCaret")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "otpCaretAlpha",
    )
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(16.dp)
            .alpha(alpha)
            .background(color)
    )
}

@Composable
internal fun InputOTPSeparator() {
    Box(
        modifier = Modifier.size(width = 8.dp, height = 1.dp).background(MaterialTheme.styles.mutedForeground)
    )
}
