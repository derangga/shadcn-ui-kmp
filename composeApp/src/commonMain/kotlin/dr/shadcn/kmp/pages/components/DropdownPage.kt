package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.DropdownMenu
import com.shadcn.ui.components.DropdownMenuItem
import com.shadcn.ui.components.DropdownMenuSeparator
import dr.shadcn.kmp.components.Layout

@Composable
fun DropDownPage() {
    Layout {
        var showDropdown by remember { mutableStateOf(false) }

        Text(
            "Dropdown",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false },
                trigger = {
                    Button(onClick = { showDropdown = !showDropdown }) {
                        Text("Open Menu")
                    }
                }
            ) {
                DropdownMenuItem(onClick = { showDropdown = false /* Handle New Team */ }) {
                    Text("New Team")
                }
                DropdownMenuItem(onClick = { showDropdown = false /* Handle Settings */ }) {
                    Text("Settings")
                }
                DropdownMenuItem(onClick = { showDropdown = false /* Handle Keyboard shortcuts */ }) {
                    Text("Keyboard shortcuts")
                }
                DropdownMenuSeparator()
                DropdownMenuItem(onClick = { showDropdown = false /* Handle GitHub */ }) {
                    Text("GitHub")
                }
                DropdownMenuItem(onClick = { showDropdown = false /* Handle Support */ }) {
                    Text("Support")
                }
                DropdownMenuItem(onClick = { showDropdown = false /* Handle API */ }, enabled = false) {
                    Text("API (Disabled)")
                }
                DropdownMenuSeparator()
                DropdownMenuItem(onClick = { showDropdown = false /* Handle Log out */ }) {
                    Text("Log out")
                }
            }
        }
    }
}