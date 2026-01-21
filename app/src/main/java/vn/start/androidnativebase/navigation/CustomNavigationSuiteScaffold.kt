package vn.start.androidnativebase.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource

/**
 * Custom navigation scaffold that conditionally shows the bottom bar
 * based on the current destination's configuration.
 *
 * @param appState App state containing navigation and bottom bar visibility
 * @param modifier Modifier for the scaffold
 * @param content Content to display in the scaffold
 */
@Composable
fun CustomNavigationSuiteScaffold(
    appState: AppState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    // Get bottom bar visibility from AppState (single source of truth)
    val shouldShowBottomBar = appState.shouldShowBottomBar

    Scaffold(
        modifier = modifier,
        bottomBar = {
            // Animated visibility for smooth show/hide transitions
            AnimatedVisibility(
                visible = shouldShowBottomBar,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    appState.topLevelDestinations.forEach { destination ->
                        val selected = appState.currentTopLevelDestination == destination
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                    contentDescription = stringResource(id = destination.iconTextId)
                                )
                            },
                            label = {
                                Text(stringResource(id = destination.iconTextId))
                            },
                            selected = selected,
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = Color(0xFF4F6BED),
                                selectedTextColor = Color(0xFF4F6BED),
                                indicatorColor = Color(0xFFDDE3FF),
                                unselectedIconColor = Color(0xFF757680),
                                unselectedTextColor = Color(0xFF757680)
                            )
                        )
                    }
                }
            }
        },
        content = { padding ->
            Box(modifier = Modifier.padding(padding)) {
                content()
            }
        }
    )
}
