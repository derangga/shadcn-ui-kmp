package dr.shadcn.kmp.pages.theme

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.shadcn.ui.components.AlertDialog
import com.shadcn.ui.components.AlertDialogAction
import com.shadcn.ui.components.AlertDialogCancel
import com.shadcn.ui.components.AlertDialogDescription
import com.shadcn.ui.components.AlertDialogTitle
import dr.shadcn.kmp.themes.StylesSelection

@Composable
fun ThemesPage(viewModel: ThemesViewModel) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            "Built-in Themes",
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold)
        )

        LazyColumn {
            items(StylesSelection.styles) {
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                        .clickable {
                            viewModel.showConfirmation(it)
                        },
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(it)
                    if (it == state.selected) {
                        Icon(Icons.Default.Check, contentDescription = "selected")
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }

    AlertDialog(
        open = state.showDialog,
        onDismissRequest = { viewModel.cancelConfirmation() },
        title = { AlertDialogTitle { Text("Switch Themes") } },
        description = {
            AlertDialogDescription {
                Text("Change themes to ${state.pendingSelected} require restart activity, are you sure?")
            }
        },
        actions = {
            AlertDialogCancel(
                onClick = {
                    viewModel.cancelConfirmation()
                }
            ) {
                Text("No")
            }
            Spacer(modifier = Modifier.width(8.dp))
            AlertDialogAction(
                onClick = {
                    viewModel.confirmSelection(state.pendingSelected)
                }
            ) {
                Text("Yes")
            }
        }
    )
}
