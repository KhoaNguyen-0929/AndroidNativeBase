package vn.start.androidnativebase.navigation

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import vn.start.androidnativebase.R
import vn.start.dashboard.navigation.DashboardNavigation
import vn.start.designsystem.icon.AppIcon
import vn.start.focus.navigation.FocusNavigation
import vn.start.planning.navigation.PlanningNavigation

/**
 * Type for the top level destinations in the application. Contains metadata about the destination
 * that is used in the top app bar and common navigation UI.
 *
 * @param selectedIcon The icon to be displayed in the navigation UI when this destination is
 * selected.
 * @param unselectedIcon The icon to be displayed in the navigation UI when this destination is
 * not selected.
 * @param iconTextId Text that to be displayed in the navigation UI.
 * @param titleTextId Text that is displayed on the top app bar.
 * @param route The route to use when navigating to this destination.
 * @param baseRoute The highest ancestor of this destination. Defaults to [route], meaning that
 * there is a single destination in that section of the app (no nested destinations).
 */
enum class TopLevelDestination(
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    @StringRes val iconTextId: Int,
    @StringRes val titleTextId: Int,
    val route: String,
    val baseRoute: String = route,
) {
    FOCUS(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = FocusNavigation.Main.route,
        iconTextId = R.string.app_focus,
        titleTextId= R.string.app_focus,
        baseRoute = FocusNavigation.Base.route,
    ),
    PLANNING(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = PlanningNavigation.Main.route,
        iconTextId = R.string.app_planning,
        titleTextId= R.string.app_planning,
        baseRoute = PlanningNavigation.Base.route,
    ),
    DASHBOARD(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = DashboardNavigation.Main.route,
        iconTextId = R.string.app_dash_board,
        titleTextId= R.string.app_dash_board,
        baseRoute = DashboardNavigation.Base.route,
    ),
}
