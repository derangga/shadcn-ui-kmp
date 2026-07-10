package com.komoui.components

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
import androidx.compose.ui.semantics.paneTitle
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.komoui.themes.radius
import com.komoui.themes.komoTypography
import com.komoui.themes.styles

/**
 * Displays a modal dialog with a title, description, and customizable action buttons.
 *
 * @param onDismissRequest Callback invoked when the user tries to dismiss the dialog (e.g., by tapping outside).
 * @param open Boolean state controlling the visibility of the dialog.
 * @param modifier The modifier to be applied to the dialog's content area.
 * @param title The composable content for the alert dialog's title.
 * @param description The composable content for the alert dialog's description.
 * @param actions The composable content for the alert dialog's action buttons (e.g., AlertDialogAction, AlertDialogCancel).
 * @param properties Dialog behavior. Defaults to non-dismissable (no outside-tap / back-press dismiss),
 * matching shadcn's AlertDialog which can only be dismissed via an explicit action.
 */
@Composable
fun AlertDialog(
    open: Boolean,
    onDismissRequest: () -> Unit,
    title: @Composable () -> Unit,
    description: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false
    )
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius

    if (open) {
        Dialog(onDismissRequest = onDismissRequest, properties = properties) {
            Column(
                modifier = modifier
                    .semantics { paneTitle = "Alert dialog" }
                    .fillMaxWidth()
                    .background(styles.background, RoundedCornerShape(radius.lg))
                    .border(1.dp, styles.border, RoundedCornerShape(radius.lg))
                    .padding(24.dp)
            ) {
                // Header (Title and Description)
                Column(modifier = Modifier.fillMaxWidth()) {
                    ProvideTextStyle(
                        value = MaterialTheme.komoTypography.titleLarge.copy(
                            color = styles.foreground
                        )
                    ) {
                        title()
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    ProvideTextStyle(
                        value = MaterialTheme.komoTypography.body.copy(
                            color = styles.mutedForeground
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
 * Composable for an action button within a AlertDialog's `actions` slot.
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
 * Composable for a cancel button within a AlertDialog's `actions` slot.
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