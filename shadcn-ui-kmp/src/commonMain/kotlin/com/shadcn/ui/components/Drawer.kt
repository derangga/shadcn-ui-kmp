package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Drawer (Bottom Sheet) component inspired by Shadcn UI.
 * Displays a modal bottom sheet with a title, description, and customizable footer.
 *
 * @param onDismissRequest Callback invoked when the user tries to dismiss the drawer (e.g., by swiping down or tapping outside).
 * @param open Boolean state controlling the visibility of the drawer.
 * @param modifier The modifier to be applied to the drawer's content area.
 * @param title The composable content for the drawer's title.
 * @param description The composable content for the drawer's description.
 * @param footer The composable content for the drawer's footer (e.g., action buttons).
 * @param content The main content of the drawer, placed between the description and the footer.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Drawer(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(),
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
    footer: (@Composable RowScope.() -> Unit)? = null,
    showCloseButton: Boolean = false,
    shouldDismissOnBackPress: Boolean = true
) {
    val styles = MaterialTheme.styles

    if (open) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            sheetState = sheetState,
            containerColor = styles.background,
            contentColor = styles.foreground,
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp),
            dragHandle = {
                // Custom drag handle to match Shadcn's aesthetic, which is often a simple line
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .drawBehind {
                            val strokeWidthPx = 1.dp.toPx()
                            val halfStroke = strokeWidthPx / 2f
                            val cornerRadiusPx = 12.dp.toPx()

                            // Draw the straight top line segment
                            drawLine(
                                color = styles.border,
                                start = Offset(cornerRadiusPx, halfStroke),
                                end = Offset(size.width - cornerRadiusPx, halfStroke),
                                strokeWidth = strokeWidthPx
                            )

                            // Draw the top-left arc segment of the border
                            drawArc(
                                color = styles.border,
                                startAngle = 180f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = Offset(0f, 0f),
                                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                                style = Stroke(width = strokeWidthPx + 2)
                            )

                            // Draw the top-right arc segment of the border
                            drawArc(
                                color = styles.border,
                                startAngle = 270f,
                                sweepAngle = 90f,
                                useCenter = false,
                                topLeft = Offset(size.width - cornerRadiusPx * 2, 0f),
                                size = Size(cornerRadiusPx * 2, cornerRadiusPx * 2),
                                style = Stroke(width = strokeWidthPx + 2)
                            )
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .width(40.dp)
                            .height(4.dp)
                            .background(
                                styles.mutedForeground,
                                RoundedCornerShape(MaterialTheme.radius.full)
                            )
                    )
                }
            },
            modifier = Modifier,
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp)
                    .then(modifier)
            ) {
                // Header (Title and Description)
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = styles.foreground,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        ) {
                            title()
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ProvideTextStyle(
                            value = TextStyle(
                                color = styles.mutedForeground,
                                fontSize = 14.sp
                            )
                        ) {
                            description()
                        }
                    }

                    if (showCloseButton) {
                        Box(
                            modifier = Modifier
                                .offset(y = (-12).dp),
                        ) {
                            Button(
                                onClick = onDismissRequest,
                                size = ButtonSize.Icon,
                                variant = ButtonVariant.Ghost,
                                modifier = Modifier
                                    .width(24.dp)
                                    .height(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close dialog",
                                    tint = styles.mutedForeground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Main Content Slot
                Column(modifier = Modifier.fillMaxWidth(), content = content)

                Spacer(modifier = Modifier.height(24.dp))

                // Footer (Actions)
                footer?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End, // justify-end
                        verticalAlignment = Alignment.CenterVertically,
                        content = it
                    )
                }
            }
        }
    }
}

/**
 * Composable for the title of a ShadcnDrawer.
 * This should be used within the `title` slot of [Drawer].
 */
@Composable
fun DrawerTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * Composable for the description of a ShadcnDrawer.
 * This should be used within the `description` slot of [Drawer].
 */
@Composable
fun DrawerDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * Composable for an action button within a ShadcnDrawer's `footer` slot.
 * Typically used for the primary action (e.g., "Save changes").
 * Uses [Button] with `ButtonVariant.Default`.
 */
@Composable
fun DrawerAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * Composable for a cancel button within a ShadcnDrawer's `footer` slot.
 * Typically used for a secondary action (e.g., "Cancel").
 * Uses [Button] with `ButtonVariant.Outline`.
 */
@Composable
fun DrawerCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}
