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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import com.komoui.themes.radius
import com.komoui.themes.styles
import com.komoui.utils.PopupAlignment
import com.komoui.utils.rememberAnchoredPopupPositionProvider

@Composable
fun Popover(
    open: Boolean,
    onDismissRequest: () -> Unit,
    trigger: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius

    // Centers the popup under the anchor, flips it above when there's no room below, and clamps
    // it within the window.
    val positionProvider = rememberAnchoredPopupPositionProvider(gap = 8.dp, alignment = PopupAlignment.Center)

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
