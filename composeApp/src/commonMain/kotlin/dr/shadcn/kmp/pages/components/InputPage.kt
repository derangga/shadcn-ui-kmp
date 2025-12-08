package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.shadcn.ui.components.Input
import com.shadcn.ui.components.InputVariant
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout

@Composable
fun InputPage() {
    Layout {
        Text(
            "Input",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Text(
            text = "Displays a form input field or a component that looks like an input field.",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Basic usage") {
            var nameTxt by remember { mutableStateOf("") }
            Input(
                value = nameTxt,
                onValueChange = { nameTxt = it },
                placeholder = "Enter your name",
            )
        }

        ContentPageWithTitle("2. Input with leading icon and supporting text") {
            var text1 by remember { mutableStateOf("") }
            Input(
                value = text1,
                onValueChange = { text1 = it },
                placeholder = "Enter your email",
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = "Email icon") },
                variant = InputVariant.Outlined,
                supportingText = { Text("This is a helpful supporting text.") }
            )
        }

        ContentPageWithTitle("3. Input for password with trailing action") {
            var password by remember { mutableStateOf("") }
            var passwordVisible by remember { mutableStateOf(false) }
            Input(
                value = password,
                onValueChange = { password = it },
                placeholder = "Password",
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(onDone = {
                    // Handle done action, e.g., login
                    println("Password entered: $password")
                }),
                trailingIcon = {
                    val image = if (passwordVisible)
                        Icons.Filled.Visibility
                    else Icons.Filled.VisibilityOff
                    Box(
                        modifier = Modifier.clickable { passwordVisible = !passwordVisible }
                    ) {
                        Icon(imageVector = image, contentDescription = if (passwordVisible) "Hide password" else "Show password")
                    }
                }
            )
        }

        ContentPageWithTitle("4. Input underline variant and multiline") {
            var multiLineText by remember { mutableStateOf("This is a multi-line text field example.\nIt can expand to show more content.") }
            Input(
                value = multiLineText,
                onValueChange = { multiLineText = it },
                placeholder = "Enter multi-line text",
                singleLine = false,
                maxLines = 5,
                variant = InputVariant.Underlined,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Default)
            )
        }

        ContentPageWithTitle("5. Disabled input") {
            var text3 by remember { mutableStateOf("Disabled input") }
            Input(
                value = text3,
                onValueChange = { text3 = it },
                enabled = false
            )
        }

        ContentPageWithTitle("6. Read-only input") {
            var text4 by remember { mutableStateOf("Read-only input") }
            Input(
                value = text4,
                onValueChange = { text4 = it },
                readOnly = true
            )
        }

        ContentPageWithTitle("7. Input with error") {
            var text5 by remember { mutableStateOf("Input with error") }
            Input(
                value = text5,
                onValueChange = { text5 = it },
                isError = true,
                supportingText = { Text("Invalid input", color = MaterialTheme.styles.destructive) }
            )
        }
    }
}