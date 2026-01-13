package vn.start.androidnativebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import vn.start.home.navigation.HomeBaseRoute
import vn.start.home.navigation.homeSection
import vn.start.pomodoro.navigation.pomodoroSection
import vn.start.stats.navigation.statsSection
import vn.start.tasks.navigation.tasksSection

@Composable
fun AppNavHost(
    navController: NavHostController,
    appState: AppState,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeBaseRoute,
        modifier = modifier
    ) {
        homeSection()
        pomodoroSection()
        tasksSection()
        statsSection()
    }
}
