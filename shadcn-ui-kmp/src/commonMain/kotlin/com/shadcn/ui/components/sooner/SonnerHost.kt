package com.shadcn.ui.components.sooner

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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