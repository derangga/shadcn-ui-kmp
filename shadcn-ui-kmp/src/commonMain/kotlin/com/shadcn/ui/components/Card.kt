package com.shadcn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.themes.BoxShadow
import com.shadcn.ui.themes.drawShadows
import com.shadcn.ui.themes.radius
import com.shadcn.ui.themes.styles

/**
 * A Jetpack Compose Card component inspired by Shadcn UI.
 * This is the main container for a card.
 *
 * @param modifier The modifier to be applied to the card container.
 * @param radius The border radius of the card.
 * @param shadow The shadow of the card.
 * @param content The composable content of the card.
 */
@Composable
fun Card(
    modifier: Modifier = Modifier,
    radius: Dp = MaterialTheme.radius.lg,
    shadow: List<BoxShadow> = MaterialTheme.styles.shadow(),
    content: @Composable ColumnScope.() -> Unit
) {
    val styles = MaterialTheme.styles
    Box(
        modifier = Modifier
            .drawShadows(radius, shadow)
            .clip(RoundedCornerShape(radius))
            .border(1.dp, styles.border, RoundedCornerShape(radius))
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.styles.card,
                    shape = RoundedCornerShape(MaterialTheme.radius.lg)
                )
                .then(modifier), content = content
        )
    }
}

/**
 * Composable for the header section of a ShadcnCard.
 *
 * @param modifier The modifier to be applied to the header.
 * @param content The composable content of the header.
 */
@Composable
fun CardHeader(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

/**
 * Composable for the title of a Card.
 * This should be used within [CardHeader].
 *
 * @param modifier The modifier to be applied to the title text.
 * @param content The composable content of the title.
 */
@Composable
fun CardTitle(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    ProvideTextStyle(
        value = TextStyle(
            color = styles.cardForeground,
            fontWeight = FontWeight.SemiBold,
            fontSize = 18.sp
        )
    ) {
        content()
    }
}

/**
 * Composable for the description of a ShadcnCard.
 * This should be used within [CardHeader].
 *
 * @param modifier The modifier to be applied to the description text.
 * @param content The composable content of the description.
 */
@Composable
fun CardDescription(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val styles = MaterialTheme.styles
    ProvideTextStyle(
        value = TextStyle(
            color = styles.mutedForeground,
            fontSize = 14.sp
        )
    ) {
        Column(modifier = modifier) {
            content()
        }
    }
}

/**
 * Composable for the main content area of a Card.
 *
 * @param modifier The modifier to be applied to the content area.
 * @param content The composable content of the main area.
 */
@Composable
fun CardContent(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .then(modifier),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}

/**
 * Composable for the footer section of a ShadcnCard.
 *
 * @param modifier The modifier to be applied to the footer.
 * @param content The composable content of the footer.
 */
@Composable
fun CardFooter(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .then(modifier),
        horizontalAlignment = horizontalAlignment,
        content = content
    )
}
