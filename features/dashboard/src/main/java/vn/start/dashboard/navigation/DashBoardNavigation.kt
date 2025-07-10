package vn.start.dashboard.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.dashboard.ui.DashBoardScreen

@SuppressLint("MissingSerializableAnnotation")
data object DashBoardRoute // route to DashBoard screen

@SuppressLint("MissingSerializableAnnotation")
data object DashBoardBaseRoute // route to base navigation graph

fun NavController.navigateToDashBoard(navOptions: NavOptions) = navigate(route = DashBoardRoute, navOptions)

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
            DashBoardScreen()
        }
        topicDestination()
    }
}
