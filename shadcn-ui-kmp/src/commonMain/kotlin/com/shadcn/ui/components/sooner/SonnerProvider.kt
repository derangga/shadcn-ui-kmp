package com.shadcn.ui.components.sooner

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow

data class SonnerEvent(
    val message: String,
    val subMessage: String? = null,
    val action: SonnerAction? = null,
    val withDismissAction: Boolean = false,
    val variant: SonnerVariant = SonnerVariant.Default,
    val duration: SnackbarDuration = SnackbarDuration.Short
)

data class SonnerAction(
    val actionText: String,
    val execute: () -> Unit
)

object SonnerProvider {
    private val _events = Channel<SonnerEvent>()
    val events = _events.receiveAsFlow()

    suspend fun showMessage(
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
                SonnerVariant.Default,
                duration
            )
        )
    }

    suspend fun showError(
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
                SonnerVariant.Destructive,
                duration
            )
        )
    }
}