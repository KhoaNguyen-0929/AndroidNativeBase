package vn.start.planning.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.planning.ui.PlanningScreen


data object PlanningRoute {
    const val route = "planning_route"
}

data object PlanningBaseRoute {
    const val route = "planning_base_route"
}

fun NavController.navigateToPlanning(navOptions: NavOptions? = null) {
    navigate(PlanningRoute.route, navOptions)
}

fun NavGraphBuilder.planningSection(
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation(
        startDestination = PlanningRoute.route,
        route = PlanningBaseRoute.route
    ) {
        composable(PlanningRoute.route) {
            PlanningScreen()
        }
        topicDestination()
    }
}
