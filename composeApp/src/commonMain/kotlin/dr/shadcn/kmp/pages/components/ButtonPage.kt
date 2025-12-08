package dr.shadcn.kmp.pages.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.ButtonSize
import com.shadcn.ui.components.ButtonVariant
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun ButtonPage() {
    Layout {
        Text(
            "Button",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Button Normal") {
            Button(
                onClick = { },
            ) { Text("Normal") }
        }

        ContentPageWithTitle("2. Button Destructive") {
            Button(
                onClick = { },
                variant = ButtonVariant.Destructive,
            ) { Text("Destructive") }
        }

        ContentPageWithTitle("3. Button Secondary") {
            Button(
                onClick = { },
                variant = ButtonVariant.Secondary,
            ) { Text("Secondary") }
        }

        ContentPageWithTitle("4. Button Link") {
            Button(
                onClick = { },
                variant = ButtonVariant.Link,
            ) { Text("Link") }
        }

        ContentPageWithTitle("5. Button Ghost") {
            Button(
                onClick = { },
                variant = ButtonVariant.Ghost,
            ) { Text("Ghost") }
        }

        ContentPageWithTitle("6. Button Outline") {
            Button(
                onClick = { },
                variant = ButtonVariant.Outline,
            ) { Text("Outline") }
        }

        ContentPageWithTitle("6. Button with Loading") {
            Button(
                enabled = false,
                onClick = { },
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = MaterialTheme.styles.background,
                    strokeWidth = 2.dp
                )
                Spacer(Modifier.width(8.dp))
                Text("Loading")
            }
        }

        ContentPageWithTitle("7. Button Icon") {
            Button(
                onClick = { },
                size = ButtonSize.Icon,
                variant = ButtonVariant.Outline
            ) {
                Icon(Icons.Default.AccountCircle, contentDescription = "Icon")
            }
        }

        ContentPageWithTitle("8. Button with Icon") {
            Button(
                onClick = { },
                variant = ButtonVariant.Outline
            ) {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = "Icon")
                Spacer(Modifier.width(8.dp))
                Text("Account")
            }
        }
    }
}
