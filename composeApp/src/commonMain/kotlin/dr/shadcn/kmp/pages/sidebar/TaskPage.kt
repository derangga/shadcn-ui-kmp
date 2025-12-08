package dr.shadcn.kmp.pages.sidebar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun TaskPage(nav: NavHostController) {
    Column {
        Text(text = "Welcome to the Task!")
    }
}