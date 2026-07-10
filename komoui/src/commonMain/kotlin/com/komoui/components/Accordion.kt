package com.komoui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.komoui.themes.komoTypography
import com.komoui.themes.styles
import com.komoui.utils.komoClickable

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
 * Displays a list of collapsible items with two expansion modes.
 *
 * Works uncontrolled by default (seeded from [defaultOpenItemId]); pass [openItems]
 * together with [onOpenChange] to drive open state from the caller.
 *
 * @param items A list of [AccordionItemData] representing the accordion sections.
 * @param modifier The modifier to be applied to the accordion container.
 * @param defaultOpenItemId The ID of the item open by default (uncontrolled mode). Null if none.
 * @param singleItemExpand When true, only one item can be expanded at a time.
 * @param openItems Controlled set of open item IDs. When non-null the caller owns open state.
 * @param onOpenChange Invoked with the next set of open item IDs when the user toggles an item.
 */
@Composable
fun Accordion(
    items: List<AccordionItemData>,
    modifier: Modifier = Modifier,
    defaultOpenItemId: String? = null,
    singleItemExpand: Boolean = false,
    openItems: Set<String>? = null,
    onOpenChange: ((Set<String>) -> Unit)? = null
) {
    val styles = MaterialTheme.styles
    val chevron = remember(styles.foreground) { chevronDown(styles.foreground) }

    // Uncontrolled state; re-seeded when defaultOpenItemId or singleItemExpand change.
    var internalOpen by remember(defaultOpenItemId, singleItemExpand) {
        mutableStateOf(defaultOpenItemId?.let(::setOf) ?: emptySet())
    }
    val expandedItems = openItems ?: internalOpen

    fun setOpen(next: Set<String>) {
        val coerced = if (singleItemExpand) next.take(1).toSet() else next
        if (openItems == null) internalOpen = coerced
        onOpenChange?.invoke(coerced)
    }

    Column(modifier = modifier.fillMaxWidth()) {
        items.forEach { item ->
            val isExpanded = expandedItems.contains(item.id)
            val rotation by animateFloatAsState(
                targetValue = if (isExpanded) 180f else 0f,
                animationSpec = tween(300), label = "chevronRotation"
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                        .komoClickable(
                            role = Role.Button,
                            stateDescription = if (isExpanded) "Expanded" else "Collapsed",
                            onClick = {
                                val next = if (singleItemExpand) {
                                    if (isExpanded) emptySet() else setOf(item.id)
                                } else {
                                    if (isExpanded) expandedItems - item.id else expandedItems + item.id
                                }
                                setOpen(next)
                            },
                        )
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // Header content
                    ProvideTextStyle(
                        value = MaterialTheme.komoTypography.titleMedium.copy(
                            color = styles.foreground
                        )
                    ) {
                        item.header()
                    }

                    Icon(
                        imageVector = chevron,
                        // Decorative: the header row already announces expanded/collapsed state.
                        contentDescription = null,
                        tint = styles.foreground,
                        modifier = Modifier
                            .rotate(rotation)
                            .width(24.dp)
                            .height(24.dp)
                    )
                }

                // Accordion Content
                AnimatedVisibility(
                    visible = isExpanded,
                    enter = fadeIn(animationSpec = tween(300)) + expandVertically(animationSpec = tween(300)),
                    exit = fadeOut(animationSpec = tween(300)) + shrinkVertically(animationSpec = tween(300))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp, bottom = 12.dp)
                    ) {
                        ProvideTextStyle(
                            value = MaterialTheme.komoTypography.body.copy(
                                color = styles.foreground
                            )
                        ) {
                            item.content()
                        }
                    }
                }
            }
        }
    }
}

private fun chevronDown(color: androidx.compose.ui.graphics.Color): ImageVector =
    ImageVector.Builder(
        name = "ChevronDown",
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).apply {
        path(
            fill = null,
            stroke = SolidColor(color),
            strokeLineWidth = 2f,
            strokeLineCap = androidx.compose.ui.graphics.StrokeCap.Round,
            strokeLineJoin = androidx.compose.ui.graphics.StrokeJoin.Round
        ) {
            moveTo(6f, 9f)
            lineTo(12f, 15f)
            lineTo(18f, 9f)
        }
    }.build()