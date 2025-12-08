package com.shadcn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.styles

/**
 * Data class to represent an individual item in the Accordion.
 * @param id A unique identifier for the accordion item.
 * @param header The composable content for the accordion item's header.
 * @param content The composable content that will be expanded/collapsed.
 */
data class AccordionItemData(
    val id: String,
    val header: @Composable () -> Unit,
    val content: @Composable () -> Unit
)

/**
 * It allows displaying a list of collapsible items with two expansion modes.
 *
 * @param items A list of [AccordionItemData] representing the accordion sections.
 * @param modifier The modifier to be applied to the accordion container.
 * @param defaultOpenItemId The ID of the item that should be open by default. Null if none.
 * @param singleItemExpand When true, only one item can be expanded at a time. When false, multiple items can be expanded.
 */
@Composable
fun Accordion(
    items: List<AccordionItemData>,
    modifier: Modifier = Modifier,
    defaultOpenItemId: String? = null,
    singleItemExpand: Boolean = false
) {
    val styles = MaterialTheme.styles
    // Use a Set to track multiple expanded items when singleItemExpand is false
    var expandedItems by remember {
        mutableStateOf(
            if (singleItemExpand && defaultOpenItemId != null) setOf(defaultOpenItemId)
            else if (!singleItemExpand && defaultOpenItemId != null) setOf(defaultOpenItemId)
            else emptySet()
        )
    }

    Column(modifier = modifier.fillMaxWidth()) {
        items.forEachIndexed { index, item ->
            val isExpanded = expandedItems.contains(item.id)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .animateContentSize(animationSpec = tween(durationMillis = 300))
                    .drawBehind {
                        val strokeWidth = 1.dp.toPx()
                        drawLine(
                            color = styles.border,
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    }
            ) {
                // Accordion Trigger (Header)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (singleItemExpand) {
                                // Single item expansion mode
                                expandedItems = if (isExpanded) emptySet() else setOf(item.id)
                            } else {
                                // Multiple items expansion mode
                                expandedItems = if (isExpanded) {
                                    expandedItems - item.id
                                } else {
                                    expandedItems + item.id
                                }
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header content
                    ProvideTextStyle(
                        value = TextStyle(
                            color = styles.foreground,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ) {
                        item.header()
                    }

                    Icon(
                        imageVector = ImageVector.Builder(
                            name = "ChevronDown",
                            defaultWidth = 24.dp,
                            defaultHeight = 24.dp,
                            viewportWidth = 24f,
                            viewportHeight = 24f
                        ).apply {
                            path(
                                fill = null,
                                stroke = SolidColor(styles.foreground),
                                strokeLineWidth = 2f,
                                strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Round,
                                strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Round
                            ) {
                                moveTo(6f, 9f)
                                lineTo(12f, 15f)
                                lineTo(18f, 9f)
                            }
                        }.build(),
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = styles.foreground,
                        modifier = Modifier
                            .rotate(if (isExpanded) 180f else 0f)
                            .width(24.dp)
                            .height(24.dp)
                    )
                }

                // Accordion Content
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(
                        animationSpec = tween(
                            300
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(
                        animationSpec = tween(
                            300
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        ProvideTextStyle(
                            value = TextStyle(
                                color = styles.foreground,
                                fontSize = 14.sp
                            )
                        ) {
                            item.content()
                        }
                    }
                }
            }
            // Add a horizontal line separator between items, except for the last one
            if (index < items.size - 1) {
                Spacer(modifier = Modifier.height(0.dp))
            }
        }
    }
}