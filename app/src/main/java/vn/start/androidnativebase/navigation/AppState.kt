package vn.start.androidnativebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import kotlinx.coroutines.CoroutineScope
import vn.start.home.navigation.navigateToHome
import vn.start.pomodoro.navigation.navigateToPomodoro
import vn.start.stats.navigation.navigateToStats
import vn.start.tasks.navigation.navigateToTasks

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
): AppState {
    return remember(navController, coroutineScope) {
        AppState(navController, coroutineScope)
    }
}

@Stable
class AppState(
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentBackStackEntry: androidx.navigation.NavBackStackEntry?
        @Composable get() {
            return navController.currentBackStackEntryFlow
                .collectAsState(initial = null).value
        }

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() {
            val currentDestinationRoute = currentDestination?.route ?: return null
            return TopLevelDestination.entries.firstOrNull { destination ->
                currentDestinationRoute.contains(destination.route) || currentDestinationRoute.contains(destination.baseRoute)
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHome(topLevelNavOptions)
            TopLevelDestination.POMODORO -> navController.navigateToPomodoro(topLevelNavOptions)
            TopLevelDestination.TASKS -> navController.navigateToTasks(topLevelNavOptions)
            TopLevelDestination.STATS -> navController.navigateToStats(topLevelNavOptions)
        }
    }

}
