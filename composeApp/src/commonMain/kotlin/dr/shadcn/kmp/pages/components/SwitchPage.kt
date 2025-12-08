package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Switch
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun SwitchPage() {
    Layout {
        Text(
            "Switch",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A control that allows the user to toggle between checked and not checked",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic Switch") {
            var switchChecked1 by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(
                    checked = switchChecked1,
                    onCheckedChange = { switchChecked1 = it }
                )
                Text("Enable feature", color = MaterialTheme.styles.foreground)
            }
        }

        ContentPageWithTitle("2. Checked Switch") {
            var switchChecked2 by remember { mutableStateOf(true) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(
                    checked = switchChecked2,
                    onCheckedChange = { switchChecked2 = it }
                )
                Text("Receive notifications", color = MaterialTheme.styles.foreground)
            }
        }

        ContentPageWithTitle("3. Disabled Switch (Unchecked)") {
            var switchChecked3 by remember { mutableStateOf(false) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(
                    checked = switchChecked3,
                    onCheckedChange = { switchChecked3 = it },
                    enabled = false
                )
                Text("Disabled option", color = MaterialTheme.styles.mutedForeground)
            }
        }

        ContentPageWithTitle("4. Disabled Switch (Checked)") {
            var switchChecked4 by remember { mutableStateOf(true) }
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Switch(
                    checked = switchChecked4,
                    onCheckedChange = { switchChecked4 = it },
                    enabled = false
                )
                Text("Disabled active option", color = MaterialTheme.styles.mutedForeground)
            }
        }
    }
}