package vn.start.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.dashboard.ui.DashboardScreen

data object DashBoardRoute {
    const val route = "dashboard_route"
}

data object DashBoardBaseRoute {
    const val route = "dashboard_base"
}

fun NavController.navigateToDashBoard(navOptions: NavOptions) =
    navigate(route = DashBoardRoute.route, navOptions)

/**
 *  The DashBoard section of the app. It can also display information about topics.
 *  This should be supplied from a separate module.
 *
 *  @param onTopicClick - Called when a topic is clicked, contains the ID of the topic
 *  @param topicDestination - Destination for topic content
 */
fun NavGraphBuilder.dashBoardSection(
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation(
        startDestination = DashBoardRoute.route,
        route = DashBoardBaseRoute.route
    ) {
        composable(DashBoardRoute.route) {
            DashboardScreen()
        }
        topicDestination()
    }
}
