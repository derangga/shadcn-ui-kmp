package com.shadcn.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import com.shadcn.ui.themes.styles

/**
 * Displays a user's profile picture or a fallback if the image is not available or fails to load.
 *
 * @param model Either an [coil3.request.ImageRequest] or the [coil3.request.ImageRequest.data] value.
 * @param modifier Optional [Modifier] for this Avatar.
 * @param size The target size of the Avatar. Defaults to 40.dp.
 *   on [fallbackText], [loadingContent], and [errorContent].
 * @param contentDescription Text used by accessibility services to describe what this image
 * @param fallbackText Text to display if the [model] is not provided or fails to load,
 *   and no specific [errorContent] is provided. This text is often
 *   initials or a placeholder character.
 * @param loadingContent An optional composable lambda to display while the image from [model]
 *   is loading. If null, a default loading indicator (or nothing) might be shown,
 *   or it might proceed directly to [fallbackText] or [errorContent]
 *   depending on the image loading implementation.
 * @param errorContent An optional composable lambda to display if loading the image from
 *   [model] fails. If null, [fallbackText] will typically be shown.
 * @param contentScale How the image (if loaded from [model]) should be scaled inside
 *   the bounds of the Avatar. Defaults to [ContentScale.Crop].
 */
@Composable
fun Avatar(
    model: Any?,
    modifier: Modifier = Modifier,
    size: Dp = 40.dp,
    contentDescription: String? = null,
    fallbackText: String,
    loadingContent: @Composable (() -> Unit)? = null,
    errorContent: @Composable (() -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop
) {
    val styles = MaterialTheme.styles

    Box(
        modifier = Modifier
            .size(size)
            .clip(CircleShape)
            .border(1.dp, styles.border, CircleShape)
            .then(modifier),
        contentAlignment = Alignment.Center
    ) {
        SubcomposeAsyncImage(
            model = model,
            modifier = Modifier.fillMaxSize(),
            contentScale = contentScale,
            contentDescription = contentDescription,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (loadingContent != null) {
                        loadingContent()
                    } else {
                        Text(
                            text = fallbackText,
                            style = TextStyle(
                                color = styles.mutedForeground,
                                fontSize = (size.value * 0.4).sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            },
            error = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (errorContent != null) {
                        errorContent()
                    } else {
                        Text(
                            text = fallbackText,
                            style = TextStyle(
                                color = styles.mutedForeground,
                                fontSize = (size.value * 0.4).sp,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        )
    }
}
