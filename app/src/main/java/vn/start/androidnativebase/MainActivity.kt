package vn.start.androidnativebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import vn.start.androidnativebase.navigation.AppNavHost
import vn.start.androidnativebase.ui.theme.AndroidNativeBaseTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidNativeBaseTheme {
                val navController = rememberNavController()
                AppNavHost(navController = navController)
            }
        }
    }
}