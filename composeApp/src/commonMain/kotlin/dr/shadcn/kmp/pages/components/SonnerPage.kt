package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.ButtonVariant
import com.shadcn.ui.components.sooner.SonnerAction
import com.shadcn.ui.components.sooner.SonnerProvider
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlinx.coroutines.launch

@Composable
fun SonnerPage() {
    val scope = rememberCoroutineScope()

    Layout {
        Text(
            "Sonner",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Sonner (snackbar)") {
            Button(
                onClick = {
                    scope.launch {
                        SonnerProvider.showMessage(
                            "This is a snackbar",
                            "Something important just happened."
                        )
                    }
                }
            ) {
                Text("Open snackbar")
            }
        }

        ContentPageWithTitle("2. Sonner with action") {
            Button(
                onClick = {
                    scope.launch {
                        SonnerProvider.showMessage(
                            "This is a snackbar",
                            "Something important just happened.",
                            action = SonnerAction("Action") {}
                        )
                    }
                }
            ) {
                Text("Open snackbar with action")
            }
        }

        ContentPageWithTitle("3. Sonner destructive") {
            Button(
                variant = ButtonVariant.Destructive,
                onClick = {
                    scope.launch {
                        SonnerProvider.showError(
                            "This is error event",
                            "Something went wrong with your request",
                            withDismissAction = true,
                        )
                    }
                }
            ) {
                Text("Open snackbar")
            }
        }
    }
}