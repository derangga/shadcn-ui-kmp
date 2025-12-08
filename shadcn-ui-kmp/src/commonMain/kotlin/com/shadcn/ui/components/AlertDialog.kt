package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * Displays a modal dialog with a title, description, and customizable action buttons.
 *
 * @param onDismissRequest Callback invoked when the user tries to dismiss the dialog (e.g., by tapping outside).
 * @param open Boolean state controlling the visibility of the dialog.
 * @param modifier The modifier to be applied to the dialog's content area.
 * @param title The composable content for the alert dialog's title.
 * @param description The composable content for the alert dialog's description.
 * @param actions The composable content for the alert dialog's action buttons (e.g., AlertDialogAction, AlertDialogCancel).
 */
@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit,
    open: Boolean,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius

    if (open) {
        Dialog(onDismissRequest = onDismissRequest) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(styles.background, RoundedCornerShape(radius.lg))
                    .border(1.dp, styles.border, RoundedCornerShape(radius.lg))
                    .padding(24.dp)
            ) {
                // Header (Title and Description)
                Column(modifier = Modifier.fillMaxWidth()) {
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
                Spacer(modifier = Modifier.height(24.dp))

                // Footer (Actions)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End, // justify-end
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    actions()
                }
            }
        }
    }
}

/**
 * Composable for the title of a AlertDialog.
 * This should be used within the `title` slot of [AlertDialog].
 */
@Composable
fun AlertDialogTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * Composable for the description of a AlertDialog.
 * This should be used within the `description` slot of [AlertDialog].
 */
@Composable
fun AlertDialogDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(modifier = modifier) {
        content()
    }
}

/**
 * Composable for an action button within a ShadcnAlertDialog's `actions` slot.
 * Typically used for the primary action (e.g., "Continue", "Confirm").
 * Uses [Button] with `ButtonVariant.Default`.
 */
@Composable
fun AlertDialogAction(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Default) {
        content()
    }
}

/**
 * Composable for a cancel button within a ShadcnAlertDialog's `actions` slot.
 * Typically used for a secondary action (e.g., "Cancel").
 * Uses [Button] with `ButtonVariant.Outline`.
 */
@Composable
fun AlertDialogCancel(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Button(onClick = onClick, modifier = modifier, variant = ButtonVariant.Outline) {
        content()
    }
}