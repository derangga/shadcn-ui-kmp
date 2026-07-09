package com.komoui.components.sonner

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Represents a Sonner snackbar event to be dispatched through [SonnerProvider].
 *
 * @property message The primary message to display.
 * @property subMessage Optional secondary message displayed below the primary message.
 * @property action Optional action button configuration.
 * @property withDismissAction Whether to show a dismiss button.
 * @property variant The visual variant of the snackbar.
 * @property duration The display duration of the snackbar.
 */
data class SonnerEvent(
    val message: String,
    val subMessage: String? = null,
    val action: SonnerAction? = null,
    val withDismissAction: Boolean = false,
    val variant: SonnerVariant = SonnerVariant.Default,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

/**
 * Defines an action button for a Sonner snackbar.
 *
 * @property actionText The label displayed on the action button.
 * @property execute The callback invoked when the action button is clicked.
 */
data class SonnerAction(
    val actionText: String,
    val execute: () -> Unit
)

/**
 * Singleton provider for dispatching Sonner snackbar events.
 *
 * Exposes a [events] flow that [SonnerHost] observes to display snackbars.
 * Use [showMessage] for informational notifications and [showError] for error/destructive notifications.
 */
object SonnerProvider {
    // Buffered so show* callers don't suspend indefinitely when no host is
    // collecting (e.g. app backgrounded and repeatOnLifecycle cancelled collection).
    private val _events = Channel<SonnerEvent>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private suspend fun show(
        variant: SonnerVariant,
        message: String,
        subMessage: String? = null,
        action: SonnerAction? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        _events.send(
            SonnerEvent(
                message,
                subMessage,
                action,
                withDismissAction,
                variant,
                duration
            )
        )
    }

    suspend fun showMessage(
        message: String,
        subMessage: String? = null,
        action: SonnerAction? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(SonnerVariant.Default, message, subMessage, action, withDismissAction, duration)
    }

    suspend fun showError(
        message: String,
        subMessage: String? = null,
        action: SonnerAction? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration = SnackbarDuration.Short
    ) {
        show(SonnerVariant.Destructive, message, subMessage, action, withDismissAction, duration)
    }
}