package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Slider
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlin.math.roundToInt

@Composable
fun SliderPage() {
    Layout {
        Text(
            "Slider",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "An input where the user selects a value from within a given range.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic slider") {
            var sliderValue1 by remember { mutableFloatStateOf(0f) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Slider(
                    value = sliderValue1,
                    onValueChange = { sliderValue1 = it },
                    valueRange = 0f..20f,
                    steps = 0
                )
                Text(
                    text = "Value: ${sliderValue1.roundToInt()}",
                    color = MaterialTheme.styles.foreground,
                    fontSize = 14.sp
                )
            }
        }

        ContentPageWithTitle("2. Slider with steps") {
            var sliderValue2 by remember { mutableFloatStateOf(50f) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Slider(
                    value = sliderValue2,
                    onValueChange = { sliderValue2 = it },
                    valueRange = 0f..100f,
                    steps = 9 // 10 steps from 0 to 100 (0, 10, 20, ..., 100)
                )
                Text(
                    text = "Value: ${sliderValue2.toInt()}",
                    color = MaterialTheme.styles.foreground,
                    fontSize = 14.sp
                )
            }
        }

        ContentPageWithTitle("3. Disabled Slider") {
            var sliderValue3 by remember { mutableFloatStateOf(0.7f) }
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Slider(
                    value = sliderValue3,
                    onValueChange = { sliderValue3 = it },
                    enabled = false
                )
                Text(
                    text = "Disabled Value: ${sliderValue3.roundToInt()}",
                    color = MaterialTheme.styles.mutedForeground,
                    fontSize = 14.sp
                )
            }
        }
    }
}