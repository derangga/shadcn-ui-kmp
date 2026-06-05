package com.komoui.demo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.komoui.demo.TopLevelRoute
import com.komoui.demo.themes.AppPreferences
import com.komoui.demo.pages.MainLayout
import com.komoui.demo.pages.MainViewModel
import com.komoui.demo.pages.components.SidebarFloatingPage
import com.komoui.demo.pages.components.SidebarInsetPage
import com.komoui.demo.pages.components.SidebarLayoutPage
import com.komoui.demo.pages.components.SidebarOffcanvasPage

@Composable
fun AppNavigation(
    navController: NavHostController,
    prefs: AppPreferences,
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

        composable(TopLevelRoute.SidebarOffcanvasGraph.path) {
            SidebarOffcanvasPage()
        }

        composable(TopLevelRoute.SidebarFloatingGraph.path) {
            SidebarFloatingPage()
        }

//        composable(TopLevelRoute.WebviewGraph.path) { backstack ->
//            val route = backstack.toRoute<String>()
//            val slug = navBackStackEntry.arguments?.getString("slug") ?: "Documentation"
//            WebviewPage(WebViewSlug.valueOf(slug))
//        }
    }
}
