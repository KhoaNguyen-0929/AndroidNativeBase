package vn.start.focus.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import kotlinx.serialization.Serializable
import vn.start.focus.ui.FocusScreen

@Serializable
data object FocusRoute // route to Focus screen

@Serializable
data object FocusBaseRoute // route to base navigation graph

fun NavController.navigateToFocus(navOptions: NavOptions) = navigate(route = FocusRoute, navOptions)

/**
 *  The Focus section of the app. It can also display information about topics.
 *  This should be supplied from a separate module.
 *
 *  @param onTopicClick - Called when a topic is clicked, contains the ID of the topic
 *  @param topicDestination - Destination for topic content
 */
fun NavGraphBuilder.focusSection(
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation<FocusBaseRoute>(startDestination = FocusRoute) {
        composable<FocusRoute>(
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
            FocusScreen()
        }
        topicDestination()
    }
}
