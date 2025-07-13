package vn.start.androidnativebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import vn.start.focus.navigation.FocusBaseRoute

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = FocusBaseRoute.route,
        modifier = modifier
    ) {

    }
}
