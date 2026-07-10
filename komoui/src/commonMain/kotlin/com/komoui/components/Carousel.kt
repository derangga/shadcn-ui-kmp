package com.komoui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import com.komoui.themes.styles
import kotlinx.coroutines.delay

/**
 * Orientation for the Carousel component.
 */
enum class CarouselOrientation {
    Horizontal,
    Vertical
}

/**
 * A Jetpack Compose Carousel component for KomoUI, utilizing HorizontalPager/VerticalPager for snapping behavior.
 *
 * @param containerModifier The modifier to be applied to the outer carousel container (Column).
 * @param modifier The modifier to be applied to the pager.
 * @param state Optional [PagerState] for external control of the pager (e.g., programmatic scrolling). If null, an internal state is created.
 * @param autoScroll If true, the carousel will automatically scroll to the next page after a delay.
 * @param autoScrollDelayMillis The delay in milliseconds between auto-scrolls. Only effective if [autoScroll] is true.
 * @param orientation The scroll orientation of the carousel. Defaults to [CarouselOrientation.Horizontal].
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
    itemCount: Int,
    modifier: Modifier = Modifier,
    containerModifier: Modifier = Modifier,
    state: PagerState? = null,
    autoScroll: Boolean = false,
    autoScrollDelayMillis: Long = 3000,
    orientation: CarouselOrientation = CarouselOrientation.Horizontal,
    componentSpacing: Dp = 8.dp,
    contentPadding: PaddingValues = PaddingValues(12.dp, 0.dp),
    showIndicator: Boolean = false,
    indicatorStyle: IndicatorStyle = CarouselDefaults.carouselIndicator(),
    itemSpacing: Dp = 8.dp,
    pageSize: PageSize = PageSize.Fill,
    onItemChanged: ((Int) -> Unit)? = null,
    content: @Composable PagerScope.(position: Int) -> Unit
) {
    // rememberPagerState must be called unconditionally (never inside `?:`),
    // otherwise toggling state null/non-null hits conditional-remember behavior.
    val internalState = rememberPagerState { itemCount }
    val pagerState = state ?: internalState

    // Invoke onItemChanged callback when the current page changes
    LaunchedEffect(pagerState.currentPage) {
        onItemChanged?.invoke(pagerState.currentPage)
    }

    // Auto-scroll: delay, then advance only if not actively being dragged. Because
    // animateScrollToPage suspends until it settles, the loop never observes (and so
    // never cancels) its own in-flight animation. Keyed on all inputs so changes take effect.
    LaunchedEffect(pagerState, autoScroll, autoScrollDelayMillis, itemCount) {
        if (!autoScroll || itemCount <= 1) return@LaunchedEffect
        while (true) {
            delay(autoScrollDelayMillis)
            if (!pagerState.isScrollInProgress) {
                val nextPage = (pagerState.currentPage + 1) % itemCount
                pagerState.animateScrollToPage(nextPage)
            }
        }
    }

    Column(
        modifier = containerModifier,
        verticalArrangement = Arrangement.spacedBy(componentSpacing)
    ) {
        when (orientation) {
            CarouselOrientation.Horizontal -> {
                HorizontalPager(
                    state = pagerState,
                    modifier = modifier,
                    contentPadding = contentPadding,
                    pageSize = pageSize,
                    pageSpacing = itemSpacing,
                    pageContent = content,
                )
            }
            CarouselOrientation.Vertical -> {
                VerticalPager(
                    state = pagerState,
                    modifier = modifier,
                    contentPadding = contentPadding,
                    pageSize = pageSize,
                    pageSpacing = itemSpacing,
                    pageContent = content,
                )
            }
        }

        if (showIndicator) {
            // Use the pager's own page count so an external state and the indicator agree.
            CarouselIndicator(pagerState, pagerState.pageCount, indicatorStyle)
        }
    }
}

@Composable
internal fun CarouselIndicator(state: PagerState, size: Int, style: IndicatorStyle) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(size) {
            val isActive = state.currentPage == it
            val dimens by animateDpAsState(
                targetValue = if (isActive) style.activeSize else style.inactiveSize
            )
            val color by animateColorAsState(
                targetValue = if (isActive) style.activeColor else style.inactiveColor
            )
            Box(
                modifier = Modifier
                    .padding(style.spacing)
                    .size(style.activeSize),
                contentAlignment = Alignment.Center
            ) {
                Spacer(
                    modifier = Modifier
                        .size(dimens)
                        .background(
                            color = color,
                            shape = style.shape
                        )
                )
            }
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