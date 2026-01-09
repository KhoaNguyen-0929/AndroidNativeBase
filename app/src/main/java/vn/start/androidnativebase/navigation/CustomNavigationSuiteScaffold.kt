/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package vn.start.androidnativebase.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Navigation scaffold that adapts to different screen sizes.
 * Shows navigation rail on larger screens and navigation bar on smaller screens.
 */
@Composable
fun CustomNavigationSuiteScaffold(
    appState: AppState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        bottomBar = {
            NavigationBar {
                appState.topLevelDestinations.forEach { destination ->
                    val selected = appState.currentTopLevelDestination == destination
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = if (selected) destination.selectedIcon else destination.unselectedIcon,
                                contentDescription = destination.name
                            )
                        },
                        label = { Text(destination.name) },
                        selected = selected,
                        onClick = { appState.navigateToTopLevelDestination(destination) }
                    )
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
