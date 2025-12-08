package dr.shadcn.kmp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import dr.shadcn.kmp.SidebarRoute
import dr.shadcn.kmp.pages.sidebar.DashboardPage
import dr.shadcn.kmp.pages.sidebar.ProjectPage
import dr.shadcn.kmp.pages.sidebar.TaskPage

@Composable
fun SidebarNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = SidebarRoute.Dashboard.path,
    ) {
        composable(SidebarRoute.Dashboard.path) {
            DashboardPage(navController)
        }
        composable(SidebarRoute.Project.path) {
            ProjectPage(navController)
        }
        composable(SidebarRoute.Task.path) {
            TaskPage(navController)
        }
    }
}
