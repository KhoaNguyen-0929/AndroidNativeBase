package vn.start.dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import vn.start.dashboard.ui.DashBoardScreen
import vn.start.dashboard.ui.DemoScreen

@Serializable
data object DashBoardRoute // route to DashBoard screen

@Serializable
data object DemoRoute // route to Demo screen

@Serializable
data object DashBoardBaseRoute // route to base navigation graph

fun NavController.navigateToDashBoard(navOptions: NavOptions) = navigate(route = DashBoardRoute, navOptions)

fun NavController.navigateToDemo(navOptions: NavOptions? = null) = navigate(route = DemoRoute, navOptions)

/**
 *  The DashBoard section of the app. It can also display information about topics.
 *  This should be supplied from a separate module.
 *
 *  @param navController - Navigation controller for navigating within this section
 *  @param onTopicClick - Called when a topic is clicked, contains the ID of the topic
 *  @param topicDestination - Destination for topic content
 */
fun NavGraphBuilder.dashBoardSection(
    navController: NavController,
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<DashBoardBaseRoute>(startDestination = DashBoardRoute) {
        composable<DashBoardRoute>(
//            deepLinks = listOf(
//                navDeepLink {
//                    /**
//                     * This destination has a deep link that enables a specific news resource to be
//                     * opened from a notification (@see SystemTrayNotifier for more). The news resource
//                     * ID is sent in the URI rather than being modelled in the route type because it's
//                     * transient data (stored in SavedStateHandle) that is cleared after the user has
//                     * opened the news resource.
//                     */
////                    uriPattern = DEEP_LINK_URI_PATTERN
//                },
//            ),
        ) {
            DashBoardScreen(navController = navController as? NavHostController)
        }

        composable<DemoRoute> {
            DemoScreen(navController = navController as? NavHostController)
        }

        topicDestination()
    }
}
