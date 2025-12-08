package dr.shadcn.kmp.pages.sidebar

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun ProjectPage(nav: NavHostController) {
    Column {
        Text(text = "Welcome to the Project!")
    }
}