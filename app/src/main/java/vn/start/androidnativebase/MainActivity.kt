package vn.start.androidnativebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.start.androidnativebase.navigation.AppNavHost
import vn.start.androidnativebase.navigation.CustomNavigationSuiteScaffold
import vn.start.androidnativebase.navigation.rememberAppState
import vn.start.androidnativebase.ui.theme.AndroidNativeBaseTheme

/**
 * Main activity for the AndroidNativeBase app.
 * Sets up the navigation host, theme, and navigation UI.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidNativeBaseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val appState = rememberAppState(navController)

                    CustomNavigationSuiteScaffold(
                        appState = appState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        AppNavHost(
                            navController = navController,
                            appState = appState
                        )
                    }
                }
            }
        }
    }
}
