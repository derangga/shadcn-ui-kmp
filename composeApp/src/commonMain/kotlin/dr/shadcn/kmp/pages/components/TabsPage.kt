package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.Input
import com.shadcn.ui.components.Tabs
import com.shadcn.ui.components.TabsContent
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.Layout

@Composable
fun TabsPage() {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Account", "Password", "Settings")
    Layout {
        Text(
            "Tabs",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Tabs(
            selectedTabIndex = selectedTab,
            onTabSelected = { selectedTab = it },
            tabs = tabs,
            modifier = Modifier.fillMaxWidth()
        ) { tabIndex ->
            TabsContent {
                when (tabIndex) {
                    0 -> {
                        AccountTab()
                    }
                    1 -> {
                        PasswordTab()
                    }
                    2 -> {
                        SettingsTab()
                    }
                }
            }
        }
    }
}

@Composable
fun AccountTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        var nameTxt by remember { mutableStateOf("John Doe") }
        var userNameTxt by remember { mutableStateOf("johndoe") }
        Text(
            text = "Account",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Make changes to your account here. Click save when you're done.",
            fontSize = 14.sp,
            color = MaterialTheme.styles.mutedForeground
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Name",
            fontWeight = FontWeight.SemiBold,
        )
        Input(
            value = nameTxt,
            onValueChange = { nameTxt = it },
            placeholder = "Enter your name",
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Username",
            fontWeight = FontWeight.SemiBold,
        )
        Input(
            value = userNameTxt,
            onValueChange = { userNameTxt = it },
            placeholder = "Enter your username",
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {}) {
            Text("Save changes")
        }
    }
}

@Composable
fun PasswordTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Password",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Change your password here. After saving, you'll be logged out.",
            fontSize = 14.sp,
            color = MaterialTheme.styles.mutedForeground
        )
    }
}

@Composable
fun SettingsTab() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Manage your account settings and preferences.",
            fontSize = 14.sp,
            color = MaterialTheme.styles.mutedForeground
        )
    }
}