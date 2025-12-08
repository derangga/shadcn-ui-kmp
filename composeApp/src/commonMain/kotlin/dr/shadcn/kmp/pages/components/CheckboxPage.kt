package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import com.shadcn.ui.components.Checkbox
import com.shadcn.ui.components.CheckboxDefaults
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun CheckboxPage() {
    Layout {
        Text(
            "Checkbox",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "A control that allows the user to toggle between checked and not checked.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Example
        ContentPageWithTitle("1. Basic Checkbox (Unchecked)") {
            var checkedState1 by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checkedState1,
                    onCheckedChange = { checkedState1 = it }
                )
                Text("Accept terms and conditions")
            }
        }

        ContentPageWithTitle("2. Basic Checkbox (Checked)") {
            var checkedState2 by remember { mutableStateOf(true) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checkedState2,
                    onCheckedChange = { checkedState2 = it }
                )
                Text("Remember me")
            }
        }

        ContentPageWithTitle("3. Disabled Checkbox (Unchecked)") {
            var checkedState3 by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checkedState3,
                    onCheckedChange = { checkedState3 = it },
                    enabled = false
                )
                Text("Disabled unchecked", color = MaterialTheme.styles.mutedForeground)
            }
        }

        ContentPageWithTitle("4. Disabled Checkbox (Checked)") {
            var checkedState4 by remember { mutableStateOf(true) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checkedState4,
                    onCheckedChange = { checkedState4 = it },
                    enabled = false
                )
                Text("Disabled checked", color = MaterialTheme.styles.mutedForeground)
            }
        }

        ContentPageWithTitle("5. Checkbox with custom size") {
            var checkedState5 by remember { mutableStateOf(false) }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Checkbox(
                    checked = checkedState5,
                    onCheckedChange = { checkedState5 = it },
                    modifier = Modifier.size(32.dp) // Larger checkbox
                )
                Text("Larger checkbox", color = MaterialTheme.styles.foreground)
            }
        }

        ContentPageWithTitle("6. Checkbox with custom colors") {
            var checkedState6 by remember { mutableStateOf(false) }
            val borderColor = if (checkedState6) {
                MaterialTheme.styles.chart3
            } else {
                MaterialTheme.styles.border
            }
            val background = if (checkedState6) {
                MaterialTheme.styles.chart1
            } else {
                MaterialTheme.styles.background
            }

            Box(
                modifier = Modifier
                    .border(1.dp, borderColor, MaterialTheme.shapes.small)
                    .background(background, MaterialTheme.shapes.small)
            ) {
                Row(
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    Checkbox(
                        checked = checkedState6,
                        onCheckedChange = { checkedState6 = it },
                        colors = CheckboxDefaults.colors().copy(
                            checkedColors = MaterialTheme.styles.chart3,
                            checkedBorderColors = MaterialTheme.styles.chart3
                        )
                    )
                    Column {
                        Text("Enable Notification", color = MaterialTheme.styles.foreground)
                        Text(
                            "You can enable or disable anytime",
                            color = MaterialTheme.styles.mutedForeground
                        )
                    }
                }
            }
        }
    }
}