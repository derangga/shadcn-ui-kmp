package com.komoui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.radius
import com.komoui.themes.styles

enum class EmptyMediaVariant {
    Default,
    Icon
}

/**
 * Container for an empty state, for KomoUI's Empty component.
 * Lays out its children in a centered column with consistent spacing.
 *
 * @param modifier The modifier to be applied to the container.
 * @param content The composable content of the empty state. Typically an [EmptyHeader] and an [EmptyContent].
 */
@Composable
fun Empty(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        content = content
    )
}

/**
 * Header section of an [Empty] state. Holds the media, title, and description.
 *
 * @param modifier The modifier to be applied to the header.
 * @param content The composable content of the header.
 */
@Composable
fun EmptyHeader(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .widthIn(max = 384.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        content = content
    )
}

/**
 * Media slot for an [Empty] state. Use [EmptyMediaVariant.Icon] to render
 * a 40dp rounded muted square around an icon, or [EmptyMediaVariant.Default]
 * to pass content (e.g. an avatar or illustration) through unchanged.
 *
 * @param modifier The modifier to be applied to the media container.
 * @param variant The visual variant of the media slot.
 * @param content The composable content of the media slot.
 */
@Composable
fun EmptyMedia(
    modifier: Modifier = Modifier,
    variant: EmptyMediaVariant = EmptyMediaVariant.Default,
    content: @Composable BoxScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    when (variant) {
        EmptyMediaVariant.Default -> {
            Box(
                modifier = modifier
                    .padding(bottom = 8.dp),
                contentAlignment = Alignment.Center,
                content = content
            )
        }

        EmptyMediaVariant.Icon -> {
            Box(
                modifier = modifier.padding(bottom = 8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(MaterialTheme.radius.lg))
                        .background(styles.muted),
                    contentAlignment = Alignment.Center
                ) {
                    CompositionLocalProvider(LocalContentColor provides styles.foreground) {
                        content()
                    }
                }
            }
        }
    }
}

/**
 * Title of an [Empty] state. Renders text with medium weight at 18sp.
 *
 * @param modifier The modifier to be applied to the title.
 * @param content The composable content of the title (typically a [androidx.compose.material3.Text]).
 */
@Composable
fun EmptyTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    ProvideTextStyle(
        value = TextStyle(
            color = styles.foreground,
            fontWeight = FontWeight.Medium,
            fontSize = 18.sp,
            letterSpacing = (-0.2).sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * Description of an [Empty] state. Renders relaxed-line-height text in the muted-foreground color.
 *
 * @param modifier The modifier to be applied to the description.
 * @param content The composable content of the description.
 */
@Composable
fun EmptyDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    ProvideTextStyle(
        value = TextStyle(
            color = styles.mutedForeground,
            fontSize = 14.sp,
            lineHeight = 22.sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * Content/action section of an [Empty] state. Use this slot for buttons or input controls
 * placed below the header.
 *
 * @param modifier The modifier to be applied to the content section.
 * @param content The composable content (e.g. buttons).
 */
@Composable
fun EmptyContent(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 384.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        content = content
    )
}
