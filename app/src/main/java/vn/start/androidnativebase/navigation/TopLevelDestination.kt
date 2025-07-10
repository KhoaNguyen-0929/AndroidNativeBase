package vn.start.androidnativebase.navigation

import androidx.compose.ui.graphics.vector.ImageVector
import vn.start.dashboard.navigation.DashBoardBaseRoute
import vn.start.dashboard.navigation.DashBoardRoute
import vn.start.designsystem.icon.AppIcon
import kotlin.reflect.KClass
import vn.start.focus.navigation.FocusRoute
import vn.start.focus.navigation.FocusBaseRoute
import vn.start.planning.navigation.PlanningBaseRoute
import vn.start.planning.navigation.PlanningRoute

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
//    @StringRes val iconTextId: Int,
//    @StringRes val titleTextId: Int,
    val route: KClass<*>,
    val baseRoute: KClass<*> = route,
) {
    FOCUS(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = FocusRoute::class,
        baseRoute = FocusBaseRoute::class,
    ),
    PLANNING(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = PlanningRoute::class,
        baseRoute = PlanningBaseRoute::class,
    ),
    DASHBOARD(
        selectedIcon = AppIcon.Person,
        unselectedIcon = AppIcon.Settings,
        route = DashBoardRoute::class,
        baseRoute = DashBoardBaseRoute::class,
    ),
}
