package vn.start.androidnativebase.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import vn.start.androidnativebase.R
import vn.start.designsystem.icon.AppIcon
import vn.start.home.navigation.HomeNavigation
import vn.start.pomodoro.navigation.PomodoroNavigation
import vn.start.stats.navigation.StatsNavigation
import vn.start.tasks.navigation.TasksNavigation

/**
 * Enum representing top-level navigation destinations.
 *
 * @param selectedIcon Icon when destination is selected
 * @param unselectedIcon Icon when destination is not selected
 * @param iconTextId String resource for icon content description
 * @param titleTextId String resource for destination title
 * @param route Route string for navigation
 * @param baseRoute Base route for nested navigation
 * @param showBottomBar Whether to show bottom navigation bar on this destination
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: String,
    val baseRoute: String = route,
    val showBottomBar: Boolean = true,
) {
    HOME(
        selectedIcon = AppIcon.Home,
        unselectedIcon = AppIcon.HomeOutline,
        route = HomeNavigation.Main.route.toString(),
        iconTextId = R.string.app_home,
        titleTextId = R.string.app_home,
        baseRoute = HomeNavigation.Base.route.toString(),
    ),
    POMODORO(
        selectedIcon = AppIcon.Timer,
        unselectedIcon = AppIcon.TimerOutline,
        route = PomodoroNavigation.Main.route.toString(),
        iconTextId = R.string.app_pomodoro,
        titleTextId = R.string.app_pomodoro,
        baseRoute = PomodoroNavigation.Base.route.toString(),
        showBottomBar = false, // Full-screen for focus mode
    ),
    TASKS(
        selectedIcon = AppIcon.Check,
        unselectedIcon = AppIcon.CheckOutline,
        route = TasksNavigation.Main.route.toString(),
        iconTextId = R.string.app_tasks,
        titleTextId = R.string.app_tasks,
        baseRoute = TasksNavigation.Base.route.toString(),
    ),
    STATS(
        selectedIcon = AppIcon.BarChart,
        unselectedIcon = AppIcon.BarChartOutline,
        route = StatsNavigation.Main.route.toString(),
        iconTextId = R.string.app_stats,
        titleTextId = R.string.app_stats,
        baseRoute = StatsNavigation.Base.route.toString(),
    ),
}
