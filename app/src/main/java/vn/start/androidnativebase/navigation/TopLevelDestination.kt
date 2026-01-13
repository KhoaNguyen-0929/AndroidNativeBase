package vn.start.androidnativebase.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import vn.start.androidnativebase.R
import vn.start.designsystem.icon.AppIcon
import vn.start.home.navigation.HomeNavigation
import vn.start.pomodoro.navigation.PomodoroNavigation
import vn.start.stats.navigation.StatsNavigation
import vn.start.tasks.navigation.TasksNavigation

enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: String,
    val baseRoute: String = route,
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
