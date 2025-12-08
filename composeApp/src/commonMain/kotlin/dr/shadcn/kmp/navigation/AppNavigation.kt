package dr.shadcn.kmp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dr.shadcn.kmp.TopLevelRoute
import dr.shadcn.kmp.pages.MainLayout
import dr.shadcn.kmp.pages.MainViewModel
import dr.shadcn.kmp.pages.components.SidebarInsetPage
import dr.shadcn.kmp.pages.components.SidebarLayoutPage

@Composable
fun AppNavigation(
    navController: NavHostController,
    prefs: DataStore<Preferences>,
    modifier: Modifier,
    isDark: Boolean
) {
    NavHost(
        navController,
        modifier = modifier,
        startDestination = TopLevelRoute.MainGraph.path
    ) {

        composable(TopLevelRoute.MainGraph.path) {
            MainLayout(
                navController,
                viewModel<MainViewModel>(factory = viewModelFactory {
                    initializer {
                        MainViewModel(
                            prefs
                        )
                    }
                }),
                isDark
            )
        }

        composable(TopLevelRoute.SidebarLayoutGraph.path) {
            SidebarLayoutPage()
        }

        composable(TopLevelRoute.SidebarInsetGraph.path) {
            SidebarInsetPage()
        }

//        composable(TopLevelRoute.WebviewGraph.path) { backstack ->
//            val route = backstack.toRoute<String>()
//            val slug = navBackStackEntry.arguments?.getString("slug") ?: "Documentation"
//            WebviewPage(WebViewSlug.valueOf(slug))
//        }
    }
}
