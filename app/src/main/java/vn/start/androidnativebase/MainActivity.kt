package vn.start.androidnativebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.start.androidnativebase.navigation.AppNavHost
import vn.start.androidnativebase.navigation.CustomNavigationSuiteScaffold
import vn.start.androidnativebase.navigation.rememberAppState
import vn.start.designsystem.theme.CustomAppTheme

/**
 * Main activity for DeepMinute app.
 * Sets up the navigation host, theme, and navigation UI.
 * Includes Android 12+ SplashScreen API integration.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Install Android 12+ SplashScreen API
        val splashScreen = installSplashScreen()

        super.onCreate(savedInstanceState)

        // Keep splash screen visible longer if needed (optional)
        // Set to false to let the system control the splash duration
        splashScreen.setKeepOnScreenCondition { false }

        enableEdgeToEdge()

        setContent {
            CustomAppTheme {
                SetSystemBarsColor()
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

/**
 * Sets system bars (status bar and navigation bar) to transparent
 * for edge-to-edge content display.
 */
@Composable
private fun SetSystemBarsColor() {
    val context = LocalContext.current
    val window = (context as ComponentActivity).window
    val colors = MaterialTheme.colorScheme

    LaunchedEffect(colors) {
        window.statusBarColor = android.graphics.Color.TRANSPARENT
        window.navigationBarColor = android.graphics.Color.TRANSPARENT
    }
}
