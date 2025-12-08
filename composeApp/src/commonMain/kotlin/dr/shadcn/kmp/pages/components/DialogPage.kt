package dr.shadcn.kmp.pages.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shadcn.ui.components.AlertDialog
import com.shadcn.ui.components.AlertDialogAction
import com.shadcn.ui.components.AlertDialogCancel
import com.shadcn.ui.components.AlertDialogDescription
import com.shadcn.ui.components.AlertDialogTitle
import com.shadcn.ui.components.Button
import com.shadcn.ui.components.Dialog
import com.shadcn.ui.components.DialogAction
import com.shadcn.ui.components.DialogCancel
import com.shadcn.ui.components.DialogDescription
import com.shadcn.ui.components.DialogTitle
import com.shadcn.ui.components.Drawer
import com.shadcn.ui.components.DrawerAction
import com.shadcn.ui.components.DrawerCancel
import com.shadcn.ui.components.DrawerDescription
import com.shadcn.ui.components.DrawerTitle
import com.shadcn.ui.components.Input
import com.shadcn.ui.themes.styles
import dr.shadcn.kmp.components.ContentPageWithTitle
import dr.shadcn.kmp.components.Layout
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialogPage() {
    var showDialog by remember { mutableStateOf(false) }
    var showFormDialog by remember { mutableStateOf(false) }
    var showAlertDialog by remember { mutableStateOf(false) }
    var showDrawer by remember { mutableStateOf(false) }

    Layout {
        Text(
            "Dialog",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        Spacer(modifier = Modifier.height(4.dp))

        ContentPageWithTitle("1. Alert Dialog") {
            Button(onClick = { showAlertDialog = true }) {
                Text("Open Alert Dialog")
            }
        }

        ContentPageWithTitle("2. Standard Dialog") {
            Button(onClick = { showDialog = true }) {
                Text("Open Dialog")
            }
        }

        ContentPageWithTitle("3. Dialog Form") {
            Button(onClick = { showFormDialog = true }) {
                Text("Open Dialog Form")
            }
        }

        ContentPageWithTitle("4. Drawer (bottom sheet)") {
            Button(onClick = { showDrawer = true }) {
                Text("Open Drawer")
            }
        }
    }

    AlertDialog(
        open = showAlertDialog,
        onDismissRequest = { showAlertDialog = false },
        title = { AlertDialogTitle { Text("Are you absolutely sure?") } },
        description = {
            AlertDialogDescription {
                Text("This action cannot be undone. This will permanently delete your account and remove your data from our servers.")
            }
        },
        actions = {
            // Order: Cancel then Action, with spacing
            AlertDialogCancel(onClick = { showAlertDialog = false }) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp)) // gap-x-2
            AlertDialogAction(onClick = {
                // Perform action
                showAlertDialog = false
            }) {
                Text("Continue")
            }
        }
    )

    Dialog(
        open = showDialog,
        onDismissRequest = { showDialog = false },
        header = {
            DialogTitle { Text("Exit application") }
            DialogDescription {
                Text("Are you sure want to close the application")
            }
        },
        footer = {
            DialogCancel(onClick = { showDialog = false }) {
                Text("No")
            }
            Spacer(modifier = Modifier.width(8.dp))
            DialogAction(onClick = { showDialog = false }) {
                Text("Yes")
            }
        }
    )

    var nameTxt by remember { mutableStateOf("John Doe") }
    var userTxt by remember { mutableStateOf("johndoe") }
    Dialog(
        open = showFormDialog,
        onDismissRequest = { showFormDialog = false },
        header = {
            DialogTitle { Text("Edit Profile") }
            DialogDescription {
                Text("Make changes to your profile here. Click save when you're done.")
            }
        },
        body = {
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
                value = userTxt,
                onValueChange = { userTxt = it },
                placeholder = "Enter your username",
                singleLine = true
            )
        },
        footer = {
            // Order: Cancel then Action, with spacing
            DialogCancel(onClick = { showFormDialog = false }) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            DialogAction(onClick = { showFormDialog = false }) {
                Text("Save changes")
            }
        }
    )

    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    Drawer(
        open = showDrawer,
        onDismissRequest = { showDrawer = false },
        sheetState = sheetState,
        title = { DrawerTitle { Text("Edit Profile") } },
        description = {
            DrawerDescription {
                Text("Make changes to your profile here. Click save when you're done.")
            }
        },
        content = {
            // Example content within the drawer
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Text(
                    text = "This is the main content area of the drawer.",
                    color = MaterialTheme.styles.foreground,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You can place any composables here, like input fields, lists, etc.",
                    color = MaterialTheme.styles.mutedForeground,
                    fontSize = 14.sp
                )
            }
        },
        footer = {
            DrawerCancel(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showDrawer = false
                    }
                }
            }) {
                Text("Cancel")
            }
            Spacer(modifier = Modifier.width(8.dp))
            DrawerAction(onClick = {
                scope.launch { sheetState.hide() }.invokeOnCompletion {
                    if (!sheetState.isVisible) {
                        showDrawer = false
                    }
                }
            }) {
                Text("Save changes")
            }
        }
    )
}