package vn.start.focus.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.focus.ui.FocusScreen


sealed class FocusNavigation(val route: String) {
    // Subgraph root
    data object Base : FocusNavigation("focus_base_route")

    data object Main : FocusNavigation("focus_route")

    data object Detail : FocusNavigation("focus_detail/{id}") {
        fun createRoute(id: String) = "focus_detail/$id"
    }
}

fun NavGraphBuilder.focusSection() {
    navigation(
        startDestination = FocusNavigation.Main.route,
        route = FocusNavigation.Base.route
    ) {
        composable(FocusNavigation.Main.route) {
            FocusScreen()
        }

        composable(FocusNavigation.Detail.route) { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("id")
//            FocusDetailScreen(id = id ?: "")
        }
    }
}

fun NavController.navigateToFocus(navOptions: NavOptions? = null) {
    navigate(FocusNavigation.Base.route, navOptions)
}

fun NavController.navigateToFocusDetail(id: String, navOptions: NavOptions? = null) {
    navigate(FocusNavigation.Detail.createRoute(id), navOptions)
}

