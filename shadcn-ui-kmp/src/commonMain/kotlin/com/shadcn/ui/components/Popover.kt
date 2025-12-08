package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles
import kotlin.math.roundToInt

/**
 * A Jetpack Compose Popover component inspired by Shadcn UI.
 * Displays a floating panel of content (popover) when triggered.
 *
 * @param open Boolean state controlling the visibility of the popover.
 * @param onDismissRequest Callback invoked when the user tries to dismiss the popover (e.g., by tapping outside).
 * @param modifier The modifier to be applied to the popover's content container.
 * @param trigger The composable content that will act as the trigger for the popover.
 * @param content The composable content to display inside the popover.
 */
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
    var triggerWidthPx by remember { mutableIntStateOf(0) }
    var triggerHeightPx by remember { mutableIntStateOf(0) }
    var triggerXPositionPx by remember { mutableIntStateOf(0) }
    var triggerYPositionPx by remember { mutableIntStateOf(0) }

    Column {
        Box(
            modifier = Modifier.onGloballyPositioned { coordinates ->
                triggerWidthPx = coordinates.size.width
                triggerHeightPx = coordinates.size.height
                val position = coordinates.parentLayoutCoordinates?.windowToLocal(coordinates.positionInWindow())
                triggerXPositionPx = position?.x?.roundToInt() ?: 0
                triggerYPositionPx = position?.y?.roundToInt() ?: 0
            }
        ) {
            trigger()
        }

        if (open) {
            Popup(
                onDismissRequest = onDismissRequest,
                alignment = Alignment.TopStart,
                offset = IntOffset((triggerXPositionPx - triggerWidthPx) / 2, triggerYPositionPx + triggerHeightPx + 12)
            ) {
                Box(
                    modifier = Modifier.shadow(1.dp, RoundedCornerShape(radius.md)) // shadow-md
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
