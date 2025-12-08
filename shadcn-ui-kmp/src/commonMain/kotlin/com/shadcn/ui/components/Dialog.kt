package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles
import androidx.compose.ui.window.Dialog as ComposeDialog

/**
 * A Jetpack Compose Dialog component inspired by Shadcn UI.
 * Displays a modal dialog with a title, description, and customizable footer.
 *
 * @param onDismissRequest Callback invoked when the user tries
 *  to dismiss the dialog (e.g., by tapping outside).
 * @param open Boolean state controlling the visibility of the dialog.
 * @param modifier The modifier to be applied to the dialog's content area.
 * @param header The composable content for the dialog's header,
 *  typically containing the title (e.g., using [DialogTitle] and [DialogDescription]).
 * @param body The composable content for the dialog's main body (e.g., input fields, lists, etc.).
 * @param footer The composable content for the dialog's footer (e.g., action buttons).
 */
@Composable
fun Dialog(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    header: (@Composable ColumnScope.() -> Unit)? = null,
    body: (@Composable ColumnScope.() -> Unit)? = null,
    footer: (@Composable RowScope.() -> Unit)? = null
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius

    if (open) {
        ComposeDialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(styles.background, RoundedCornerShape(radius.lg))
                    .border(1.dp, styles.border, RoundedCornerShape(radius.lg))
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                header?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            content = it
                        )

                        Box(
                            modifier = Modifier
                                .offset(y = (-16).dp, x = (16).dp),
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

                body?.let {
                    Column(content = it)
                }
                // Footer (Actions)
                footer?.let {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically,
                        content = it
                    )
                }
            }
        }
    }
}

/**
 * Composable for the title of a Dialog.
 * This should be used within the `title` slot of [Dialog].
 */
@Composable
fun DialogTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = TextStyle(
            color = MaterialTheme.styles.foreground,
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * Composable for the description of a Dialog.
 * This should be used within the `description` slot of [Dialog].
 */
@Composable
fun DialogDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    ProvideTextStyle(
        value = TextStyle(
            color = MaterialTheme.styles.mutedForeground,
            fontSize = 14.sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * Composable for an action button within a ShadcnDialog's `footer` slot.
 * Typically used for the primary action (e.g., "Save changes").
 * Uses [Button] with `ButtonVariant.Default`.
 */
@Composable
fun DialogAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * Composable for a cancel button within a ShadcnDialog's `footer` slot.
 * Typically used for a secondary action (e.g., "Cancel").
 * Uses [Button] with `ButtonVariant.Outline`.
 */
@Composable
fun DialogCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}
