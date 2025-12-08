package com.shadcn.ui.components.sooner

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.SnackbarVisuals

enum class SonnerVariant {
    Default,
    Destructive,
}

class SonnerVisualsImpl(
    override val message: String,
    override val actionLabel: String?,
    override val withDismissAction: Boolean,
    override val duration: SnackbarDuration,
    val subMessage: String? = null,
    val variant: SonnerVariant = SonnerVariant.Default
) : SnackbarVisuals {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SonnerVisualsImpl

        if (message != other.message) return false
        if (actionLabel != other.actionLabel) return false
        if (withDismissAction != other.withDismissAction) return false
        if (duration != other.duration) return false
        if (subMessage != other.subMessage) return false
        if (variant != other.variant) return false

        return true
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + actionLabel.hashCode()
        result = 31 * result + withDismissAction.hashCode()
        result = 31 * result + duration.hashCode()
        result = 31 * result + subMessage.hashCode()
        result = 31 * result + variant.hashCode()
        return result
    }
}

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