package com.komoui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupPositionProvider
import kotlin.math.max

/** Horizontal placement of an anchored popup relative to its trigger. */
enum class PopupAlignment { Start, Center }

/**
 * A [PopupPositionProvider] for dropdown-style popups (Select, Combobox, DatePicker, Popover).
 *
 * Anchors the popup below its trigger, flips it above when there isn't room below, and clamps it
 * horizontally so it never runs off-screen. [Compose][androidx.compose.ui.window.Popup] passes the
 * trigger's layout as `anchorBounds`, so callers don't need to measure the anchor position manually.
 *
 * @param gap Vertical gap (px) between trigger and popup.
 * @param alignment Horizontal alignment of the popup relative to the trigger.
 */
class AnchoredPopupPositionProvider(
    private val gap: Int,
    private val alignment: PopupAlignment = PopupAlignment.Start,
) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize,
    ): IntOffset {
        val rawX = when (alignment) {
            PopupAlignment.Start -> anchorBounds.left
            PopupAlignment.Center -> anchorBounds.left + (anchorBounds.width - popupContentSize.width) / 2
        }
        val x = rawX.coerceIn(0, max(0, windowSize.width - popupContentSize.width))
        val below = anchorBounds.bottom + gap
        val above = anchorBounds.top - popupContentSize.height - gap
        val fitsBelow = below + popupContentSize.height <= windowSize.height
        val y = if (fitsBelow || above < 0) below else above
        return IntOffset(x, y)
    }
}

/** Remembers an [AnchoredPopupPositionProvider], converting [gap] to pixels at the current density. */
@Composable
fun rememberAnchoredPopupPositionProvider(
    gap: Dp = 4.dp,
    alignment: PopupAlignment = PopupAlignment.Start,
): AnchoredPopupPositionProvider {
    val gapPx = with(LocalDensity.current) { gap.roundToPx() }
    return remember(gapPx, alignment) { AnchoredPopupPositionProvider(gapPx, alignment) }
}
