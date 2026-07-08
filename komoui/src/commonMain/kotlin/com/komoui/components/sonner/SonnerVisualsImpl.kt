package com.komoui.components.sonner

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals

/**
 * Defines the visual style variants for the Sonner snackbar.
 *
 * - [Default] uses the standard snackbar colors from the theme.
 * - [Destructive] uses destructive/error colors to indicate a failure or warning.
 */
enum class SonnerVariant {
    Default,
    Destructive,
}

/**
 * Implementation of [SnackbarVisuals] that carries Sonner-specific data.
 *
 * Extends the standard snackbar visuals with a [subMessage] for secondary text
 * and a [variant] to control the visual styling.
 *
 * @property message The primary message to display.
 * @property actionLabel Optional label for the action button.
 * @property withDismissAction Whether to show a dismiss button.
 * @property duration The display duration of the snackbar.
 * @property subMessage Optional secondary message displayed below the primary message.
 * @property variant The visual variant, either [SonnerVariant.Default] or [SonnerVariant.Destructive].
 */
data class SonnerVisualsImpl(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
    val subMessage: String? = null,
    val variant: SonnerVariant = SonnerVariant.Default
) : SnackbarVisuals

/**
 * Extension function on [SnackbarHostState] to display a Sonner-styled snackbar.
 *
 * Converts a [SonnerEvent] into [SonnerVisualsImpl] and shows it using the host state.
 *
 * @param event The [SonnerEvent] containing the message, variant, and action configuration.
 * @return The [SnackbarResult] indicating whether the snackbar was dismissed or the action was performed.
 */
suspend fun SnackbarHostState.showSonner(event: SonnerEvent): SnackbarResult {
    return showSnackbar(SonnerVisualsImpl(
        message = event.message,
        subMessage = event.subMessage,
        actionLabel = event.action?.actionText,
        withDismissAction = event.withDismissAction,
        duration = event.duration,
        variant = event.variant
    ))
}