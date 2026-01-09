package vn.start.androidnativebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import vn.start.dashboard.navigation.dashboardSection
import vn.start.focus.navigation.FocusBaseRoute
import vn.start.focus.navigation.focusSection
import vn.start.planning.navigation.planningSection

@Composable
fun AppNavHost(
    navController: NavHostController,
    appState: AppState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = FocusBaseRoute,
        modifier = modifier
    ) {
        focusSection(
            onTopicClick = { /* Handle topic click */ },
            topicDestination = {
                // Add topic destination here if needed
            }
        )

        dashboardSection(
            navController = navController,
            onTopicClick = { /* Handle topic click */ },
            topicDestination = {
                // Add topic destination here if needed
            }
        )

        planningSection(
            onTopicClick = { /* Handle topic click */ },
            topicDestination = {
                // Add topic destination here if needed
            }
        )
    }
}
