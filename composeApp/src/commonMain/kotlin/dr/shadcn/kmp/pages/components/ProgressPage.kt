package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Progress
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlinx.coroutines.delay

@Composable
fun ProgressPage() {
    var progress1 by remember { mutableFloatStateOf(0.0f) }
    var progress2 by remember { mutableFloatStateOf(0.75f) }
    var progress3 by remember { mutableFloatStateOf(0.0f) }

    // Simulate progress change
    LaunchedEffect(Unit) {
        while (true) {
            delay(2000)
            progress1 = (progress1 + 0.1f).coerceAtMost(1.0f)
            progress2 = (progress2 - 0.1f).coerceAtLeast(0.0f)
            progress3 = (progress3 + 0.2f) % 1.0f // Loop progress
        }
    }

    Layout {
        Text(
            "Progress",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Displays an indicator showing the completion progress of a task, typically displayed as a progress bar",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Progress with basic usage increment") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Progress: ${(progress1 * 100).toInt()}%",
                    color = MaterialTheme.styles.foreground,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
                Progress(progress = progress1)
            }
        }

        ContentPageWithTitle("2. Progress with basic usage decrement") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Progress: ${(progress2 * 100).toInt()}%",
                    color = MaterialTheme.styles.foreground,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
                Progress(progress = progress2, height = 8.dp) // Smaller height
            }
        }

        ContentPageWithTitle("3. Progress with custom color") {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Progress: ${(progress3 * 100).toInt()}%",
                    color = MaterialTheme.styles.foreground,
                    style = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
                Progress(progress = progress3, indicatorColor = MaterialTheme.styles.destructive) // Custom color
            }
        }
    }
}