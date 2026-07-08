package com.komoui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import com.komoui.themes.radius
import com.komoui.themes.styles

@Composable
fun Popover(
    open: Boolean,
    modifier: Modifier = Modifier,
    onDismissRequest: (() -> Unit)? = null,
    trigger: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val gapPx = with(LocalDensity.current) { 8.dp.roundToPx() }

    // Centers the popup horizontally under the anchor using the measured popup size,
    // so true centering works regardless of density or popup width.
    val positionProvider = remember(gapPx) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val x = anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
                val y = anchorBounds.bottom + gapPx
                return IntOffset(x, y)
            }
        }
    }

    Box {
        trigger()

        if (open) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = onDismissRequest
            ) {
                Box(
                    modifier = Modifier.shadow(4.dp, RoundedCornerShape(radius.md))
                ) {
                    Column(
                        modifier = modifier
                            .background(styles.popover, RoundedCornerShape(radius.md))
                            .border(1.dp, styles.border, RoundedCornerShape(radius.md))
                            .padding(12.dp)
                    ) {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = styles.popoverForeground,
                                fontSize = 14.sp
                            )
                        ) {
                            content()
                        }
                    }
                }
            }
        }
    }
}
