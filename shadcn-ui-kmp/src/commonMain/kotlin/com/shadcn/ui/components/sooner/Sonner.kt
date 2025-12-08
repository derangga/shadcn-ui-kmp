package com.shadcn.ui.components.sooner

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.ButtonSize
import com.shadcn.ui.components.ButtonVariant
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

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
    val containerColor = when (variant) {
        SonnerVariant.Default -> styles.snackbar
        SonnerVariant.Destructive -> styles.destructive
    }

    val contentColor = when (variant) {
        SonnerVariant.Default -> styles.foreground
        SonnerVariant.Destructive -> styles.destructiveForeground
    }

    val actionContentColor = when (variant) {
        SonnerVariant.Default -> styles.mutedForeground
        SonnerVariant.Destructive -> styles.destructiveForeground
    }

    val border = when (variant) {
        SonnerVariant.Default -> styles.border
        SonnerVariant.Destructive -> styles.destructive
    }
    Snackbar(
        modifier = modifier
            .padding(16.dp)
            .border(1.dp, border, RoundedCornerShape(radius.lg)),
        action = if (actionLabel != null && onActionClick != null && onDismiss == null) {
            {
                if (variant == SonnerVariant.Destructive) {
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .clickable(onClick = onActionClick)
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
                            .clickable(onClick = onDismiss)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "close",
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
                        Icon(Icons.Default.Close, contentDescription = "close")
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
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = TextStyle(fontSize = 14.sp)
                )
            }
        }
    }
}