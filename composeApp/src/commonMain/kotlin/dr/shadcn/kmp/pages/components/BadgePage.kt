package dr.shadcn.kmp.pages.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Badge
import com.shadcn.ui.components.BadgeVariant
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.ButtonSize
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun BadgePage() {
    val styles = MaterialTheme.styles
    Layout {
        Text(
            "Badge",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Displays a badge or a component that looks like a badge.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Default badge") {
            Badge {
                Text("Default")
            }
        }

        ContentPageWithTitle("2. Secondary badge") {
            Badge(variant = BadgeVariant.Secondary) {
                Text("Secondary")
            }
        }

        ContentPageWithTitle("3. Destructive badge") {
            Badge(variant = BadgeVariant.Destructive) {
                Text("Destructive")
            }
        }

        ContentPageWithTitle("4. Outline badge") {
            Badge(variant = BadgeVariant.Outline) {
                Text("Outline")
            }
        }

        ContentPageWithTitle("5. Custom badge") {
            Badge(backgroundColor = styles.chart3, roundedSize = 8.dp) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "icon",
                        tint = Color.White
                    )
                    Text("Custom background")
                }
            }
        }

        ContentPageWithTitle("6. Notification badge") {
            BadgedBox(
                badge = {
                    Badge(
                        variant = BadgeVariant.Destructive
                    ) {
                        Text("8")
                    }
                }
            ) {
                Button(
                    size = ButtonSize.Icon,
                    onClick = {}
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = "icon")
                }
            }
        }
    }
}
