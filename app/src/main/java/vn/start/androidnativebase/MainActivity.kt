package vn.start.androidnativebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import vn.start.androidnativebase.navigation.NiaApp
import vn.start.androidnativebase.navigation.rememberAppState
import vn.start.androidnativebase.ui.theme.AndroidNativeBaseTheme
import vn.start.data.utils.TimeZoneMonitor
import vn.start.ui.LocalTimeZone
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var timeZoneMonitor: TimeZoneMonitor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        var themeSettings by mutableStateOf(
//            ThemeSettings(
//                darkTheme = resources.configuration.isSystemInDarkTheme,
//                androidTheme = MainActivityUiState.Loading.shouldUseAndroidTheme,
//                disableDynamicTheming = MainActivityUiState.Loading.shouldDisableDynamicTheming,
//            ),
//        )
        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState(
                timeZoneMonitor = timeZoneMonitor
            )

            val currentTimeZone by appState.currentTimeZone.collectAsStateWithLifecycle()

            CompositionLocalProvider(
                LocalTimeZone provides currentTimeZone,
            ) {
                AndroidNativeBaseTheme {
                    NiaApp(appState = appState)
                }
            }
        }
    }
}

/**
 * Class for the system theme settings.
 * This wrapping class allows us to combine all the changes and prevent unnecessary recompositions.
 */
data class ThemeSettings(
    val darkTheme: Boolean,
    val androidTheme: Boolean,
    val disableDynamicTheming: Boolean,
)