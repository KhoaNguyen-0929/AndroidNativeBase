package vn.start.androidnativebase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import vn.start.androidnativebase.ui.pomodoro.PomodoroScreen
import vn.start.androidnativebase.ui.theme.AndroidNativeBaseTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidNativeBaseTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PomodoroScreen(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}