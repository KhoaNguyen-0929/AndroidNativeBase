package vn.start.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.dashboard.ui.DashboardScreen

/**
 * Navigation destinations for Dashboard feature
 */
sealed class DashboardNavigation(val route: String) {
    // Subgraph root
    data object Base : DashboardNavigation("dashboard_base_route")

    // Main screen
    data object Main : DashboardNavigation("dashboard_route")
}

fun NavController.navigateToDashboard(navOptions: NavOptions? = null) {
    navigate(DashboardNavigation.Base.route, navOptions)
}

/**
 * Dashboard section of the app
 */
fun NavGraphBuilder.dashboardSection() {
    navigation(
        startDestination = DashboardNavigation.Main.route,
        route = DashboardNavigation.Base.route,
    ) {
        composable(DashboardNavigation.Main.route) {
            DashboardScreen()
        }
    }
}
