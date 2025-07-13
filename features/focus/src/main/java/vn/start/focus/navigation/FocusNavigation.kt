package vn.start.focus.navigation

import android.annotation.SuppressLint
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import vn.start.focus.ui.FocusScreen

@SuppressLint("MissingSerializableAnnotation")
data object FocusRoute {
    const val route = "focus_route"
}

@SuppressLint("MissingSerializableAnnotation")
data object FocusBaseRoute {
    const val route = "focus_base_route"
}

fun NavController.navigateToFocus(navOptions: NavOptions? = null) {
    navigate(FocusRoute.route, navOptions)
}

fun NavGraphBuilder.focusSection(
    onTopicClick: (String) -> Unit,
    topicDestination: NavGraphBuilder.() -> Unit,
) {
    navigation(
        startDestination = FocusRoute.route,
        route = FocusBaseRoute.route
    ) {
        composable(route = FocusRoute.route) {
            FocusScreen()
        }
        topicDestination()
    }
}
