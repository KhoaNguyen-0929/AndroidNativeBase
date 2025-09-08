package vn.start.planning.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.planning.ui.PlanningScreen

/**
 * Navigation destinations for Planning feature
 */
sealed class PlanningNavigation(val route: String) {
    // Subgraph root
    data object Base : PlanningNavigation("planning_base_route")

    // Main screen
    data object Main : PlanningNavigation("planning_route")
}

fun NavController.navigateToPlanning(navOptions: NavOptions? = null) {
    navigate(PlanningNavigation.Base.route, navOptions)
}

/**
 * Planning section of the app
 */
fun NavGraphBuilder.planningSection() {
    navigation(
        startDestination = PlanningNavigation.Main.route,
        route = PlanningNavigation.Base.route,
    ) {
        composable(PlanningNavigation.Main.route) {
            PlanningScreen()
        }
    }
}
