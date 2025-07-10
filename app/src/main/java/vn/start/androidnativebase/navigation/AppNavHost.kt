package vn.start.androidnativebase.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import vn.start.focus.navigation.FocusBaseRoute
import vn.start.focus.navigation.focusSection

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = FocusBaseRoute,
        modifier = modifier
    ) {

    }
}
