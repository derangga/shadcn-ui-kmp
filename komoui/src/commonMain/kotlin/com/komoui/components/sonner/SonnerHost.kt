package com.komoui.components.sonner

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A [SnackbarHost] wrapper that renders [SonnerVisualsImpl] visuals using the [Sonner] composable.
 *
 * When the snackbar data contains [SonnerVisualsImpl], it delegates rendering to [Sonner].
 * For all other visuals, it falls back to the default Material 3 [Snackbar].
 *
 * @param hostState The [SnackbarHostState] used to control snackbar display.
 * @param modifier The [Modifier] to apply to the host.
 */

@Composable
fun SonnerHost(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(hostState, modifier = modifier) { data ->
        when (val v = data.visuals) {
            is SonnerVisualsImpl -> {
                val onDismiss: (() -> Unit)? = if (v.withDismissAction) {
                    { data.dismiss() }
                } else null
                val onActionClick: (() -> Unit)? = if (v.actionLabel != null) {
                    { data.performAction() }
                } else null

                Sonner(
                    title = v.message,
                    subtitle = v.subMessage,
                    actionLabel = v.actionLabel,
                    variant = v.variant,
                    onActionClick = onActionClick,
                    onDismiss = onDismiss
                )
            }

            else -> Snackbar(data)
        }
    }
}