package com.komoui.components.sonner

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.components.Button
import com.komoui.components.ButtonSize
import com.komoui.components.ButtonVariant
import com.komoui.themes.radius
import com.komoui.themes.KomoStyles
import com.komoui.themes.komoStrings
import com.komoui.themes.komoTypography
import com.komoui.themes.styles
import com.komoui.utils.komoClickable

/**
 * A styled snackbar component inspired by the Sonner toast library.
 *
 * Displays a notification bar with a title, optional subtitle, action button, and dismiss button.
 * Supports default and destructive variants with appropriate theming.
 *
 * @param modifier The [Modifier] to apply to the snackbar.
 * @param title The primary message displayed in the snackbar.
 * @param subtitle An optional secondary message displayed below the title.
 * @param actionLabel The text label for the optional action button.
 * @param onActionClick Callback invoked when the action button is clicked.
 * @param onDismiss Callback invoked when the dismiss button is clicked.
 * @param variant The visual variant of the snackbar, either [SonnerVariant.Default] or [SonnerVariant.Destructive].
 */
@Composable
fun Sonner(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    actionLabel: String? = null,
    onActionClick: (() -> Unit)? = null,
    onDismiss: (() -> Unit)? = null,
    variant: SonnerVariant = SonnerVariant.Default
) {
    val styles = MaterialTheme.styles
    val radius = MaterialTheme.radius
    val (containerColor, contentColor, actionContentColor, border) = resolveSonnerColors(variant, styles)
    Snackbar(
        modifier = modifier
            .semantics { liveRegion = LiveRegionMode.Polite }
            .padding(16.dp)
            .border(1.dp, border, RoundedCornerShape(radius.lg)),
        action = if (actionLabel != null && onActionClick != null) {
            {
                if (variant == SonnerVariant.Destructive) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .komoClickable(
                                onClick = onActionClick,
                                role = Role.Button,
                                shape = RoundedCornerShape(radius.sm),
                            )
                            .minimumInteractiveComponentSize()
                    ) {
                        Text(actionLabel, color = styles.destructiveForeground)
                    }
                } else {
                    Button(
                        onClick = onActionClick,
                        size = ButtonSize.Sm,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(actionLabel)
                    }
                }
            }
        } else null,
        dismissAction = if (onDismiss != null) {
            {
                if (variant == SonnerVariant.Destructive) {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .komoClickable(
                                onClick = onDismiss,
                                role = Role.Button,
                                shape = RoundedCornerShape(radius.sm),
                            )
                            .minimumInteractiveComponentSize()
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = MaterialTheme.komoStrings.close,
                            tint = styles.destructiveForeground
                        )
                    }
                } else {
                    Button(
                        onClick = onDismiss,
                        variant = ButtonVariant.Ghost,
                        size = ButtonSize.Icon,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = MaterialTheme.komoStrings.close)
                    }
                }
            }
        } else null,
        shape = RoundedCornerShape(radius.lg),
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        actionOnNewLine = false
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.komoTypography.titleEmphasis
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.komoTypography.body
                )
            }
        }
    }
}

private data class SonnerColors(
    val containerColor: Color,
    val contentColor: Color,
    val actionContentColor: Color,
    val border: Color
)

// ponytail: fixed hues; shadcn rich-color toasts are theme-independent by design.
// Promote to KomoStyles tokens if the theme grows semantic success/info/warning colors.
private fun resolveSonnerColors(
    variant: SonnerVariant,
    styles: KomoStyles
): SonnerColors = when (variant) {
    SonnerVariant.Default, SonnerVariant.Loading -> SonnerColors(
        containerColor = styles.snackbar,
        contentColor = styles.foreground,
        actionContentColor = styles.mutedForeground,
        border = styles.border
    )
    SonnerVariant.Destructive -> SonnerColors(
        containerColor = styles.destructive,
        contentColor = styles.destructiveForeground,
        actionContentColor = styles.destructiveForeground,
        border = styles.destructive
    )
    SonnerVariant.Success -> SonnerColors(
        containerColor = styles.snackbar,
        contentColor = Color(0xFF15803D),
        actionContentColor = styles.mutedForeground,
        border = Color(0xFF22C55E)
    )
    SonnerVariant.Info -> SonnerColors(
        containerColor = styles.snackbar,
        contentColor = Color(0xFF1D4ED8),
        actionContentColor = styles.mutedForeground,
        border = Color(0xFF3B82F6)
    )
    SonnerVariant.Warning -> SonnerColors(
        containerColor = styles.snackbar,
        contentColor = Color(0xFFB45309),
        actionContentColor = styles.mutedForeground,
        border = Color(0xFFF59E0B)
    )
}
