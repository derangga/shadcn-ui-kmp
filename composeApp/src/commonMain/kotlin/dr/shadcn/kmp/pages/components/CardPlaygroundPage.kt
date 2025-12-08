package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Card
import com.shadcn.ui.components.CardContent
import com.shadcn.ui.components.CardDescription
import com.shadcn.ui.components.CardFooter
import com.shadcn.ui.components.CardHeader
import com.shadcn.ui.components.CardTitle
import com.shadcn.ui.components.Slider
import com.shadcn.ui.themes.BoxShadow
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.Layout
import kotlin.math.roundToInt

@Composable
fun CardPlaygroundPage() {
    val styles = MaterialTheme.styles
    val offsetX = remember {
        mutableFloatStateOf(0f)
    }
    val offsetY = remember {
        mutableFloatStateOf(4f)
    }
    val spread = remember {
        mutableFloatStateOf(8f)
    }
    val blurRadius = remember {
        mutableFloatStateOf(8f)
    }
    val radius = remember {
        mutableFloatStateOf(8f)
    }
    Layout {
        Text("Card Playground", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        Card(
            shadow = listOf(
                BoxShadow(
                    offsetX = offsetX.floatValue.dp,
                    offsetY = offsetY.floatValue.dp,
                    blurRadius = blurRadius.floatValue.dp,
                    spread = spread.floatValue.dp,
                    color = styles.border,
                )
            ),
            radius = radius.floatValue.dp
        ) {
            CardHeader {
                CardTitle { Text("Card Title") }
                CardDescription { Text("Card Description") }
            }
            CardContent {
                Text("This is the main content of the card.")
                Spacer(modifier = Modifier.height(8.dp))
                Text("You can put any composables here.")
            }
            CardFooter {
                Text("Card Footer")
            }
        }

        Spacer(modifier = Modifier.padding(vertical = 24.dp))

        Column(
            modifier = Modifier.padding(bottom = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Header("Shadow")
            SliderWithTitle(
                title = { Text("OffsetX: ${offsetX.floatValue.roundToInt()}") },
                from = 0f,
                to = 80f,
                value = offsetX
            )
            SliderWithTitle(
                title = { Text("OffsetY: ${offsetY.floatValue.roundToInt()}") },
                from = 0f,
                to = 80f,
                value = offsetY
            )
            SliderWithTitle(
                title = { Text("Spread: ${spread.floatValue.roundToInt()}") },
                from = 0f,
                to = 80f,
                value = spread
            )
            SliderWithTitle(
                title = { Text("Blur: ${blurRadius.floatValue.roundToInt()}") },
                from = 0f,
                to = 80f,
                value = blurRadius
            )
            SliderWithTitle(
                title = { Text("Radius: ${radius.floatValue.roundToInt()}") },
                from = 0f,
                to = 80f,
                value = radius
            )
        }
    }
}

@Composable
fun Header(
    title: String
) {
    Column {
        Text(
            title,
            fontWeight = FontWeight.Black,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
}

@Composable
fun SliderWithTitle(
    title: @Composable () -> Unit,
    from: Float = 0f,
    to: Float = 1f,
    value: MutableState<Float>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        title()
        Slider(
            value = value.value,
            onValueChange = {
                value.value = it
            },
            valueRange = from..to,
        )
    }
}