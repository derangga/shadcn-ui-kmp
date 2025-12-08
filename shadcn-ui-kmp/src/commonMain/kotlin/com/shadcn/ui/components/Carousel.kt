package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.shadcn.ui.themes.styles
import kotlinx.coroutines.delay

/**
 * A Jetpack Compose Carousel component inspired by Shadcn UI, utilizing HorizontalPager for snapping behavior.
 *
 * @param modifier The modifier to be applied to the carousel container.
 * @param autoScroll If true, the carousel will automatically scroll to the next page after a delay.
 * @param autoScrollDelayMillis The delay in milliseconds between auto-scrolls. Only effective if [autoScroll] is true.
 * @param componentSpacing The spacing between carousel item and indicator components.
 * @param contentPadding The padding to be applied to the content of the pager.
 * @param showIndicator Whether to show the carousel indicator.
 * @param indicatorStyle The style of the carousel indicator.
 * @param itemSpacing The spacing between individual pages (items) in the carousel.
 * @param itemCount The total number of pages (items) in the carousel.
 * @param pageSize The size of each page (item) in the carousel.
 * @param onItemChanged A callback that is invoked when the current page (item) changes.
 * @param content The composable content for each page of the carousel.
 */
@Composable
fun Carousel(
    modifier: Modifier = Modifier,
    autoScroll: Boolean = false,
    autoScrollDelayMillis: Long = 3000,
    componentSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp, 0.dp),
    showIndicator: Boolean = false,
    indicatorStyle: IndicatorStyle = CarouselDefaults.carouselIndicator(),
    itemSpacing: Dp = 8.dp,
    itemCount: Int,
    pageSize: PageSize = PageSize.Fill,
    onItemChanged: ((Int) -> Unit)? = null,
    content: @Composable PagerScope.(position: Int) -> Unit
) {
    val pagerState = rememberPagerState { itemCount }

    // Invoke onItemChanged callback when the current page changes
    LaunchedEffect(pagerState.currentPage) {
        onItemChanged?.invoke(pagerState.currentPage)
    }

    if (autoScroll && itemCount > 1) {
        LaunchedEffect(Unit) {
            while (true) {
                delay(autoScrollDelayMillis)
                val nextPage = (pagerState.currentPage + 1) % itemCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(componentSpacing)
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = contentPadding,
                pageSize = pageSize,
                pageSpacing = itemSpacing,
                pageContent = content,
            )
        }

        if (showIndicator) {
            CarouselIndicator(pagerState, itemCount, indicatorStyle)
        }
    }
}

@Composable
fun CarouselIndicator(state: PagerState, size: Int, style: IndicatorStyle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) {
            val dimens = if (state.currentPage == it) {
                style.activeSize
            } else {
                style.inactiveSize
            }
            Spacer(
                modifier = Modifier
                    .padding(style.spacing)
                    .size(dimens)
                    .background(
                        color = if (state.currentPage == it) {
                            style.activeColor
                        } else {
                            style.inactiveColor
                        },
                        shape = style.shape
                    )
            )
        }
    }
}

data class IndicatorStyle(
    val activeColor: Color,
    val inactiveColor: Color,
    val activeSize: Dp,
    val inactiveSize: Dp,
    val shape: Shape,
    val spacing: Dp
)

object CarouselDefaults {
    @Composable
    fun carouselIndicator(): IndicatorStyle {
        return IndicatorStyle(
            activeColor = MaterialTheme.styles.foreground,
            inactiveColor = MaterialTheme.styles.mutedForeground,
            activeSize = 12.dp,
            inactiveSize = 8.dp,
            spacing = 8.dp,
            shape = CircleShape
        )
    }
}