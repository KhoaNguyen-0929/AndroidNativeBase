package vn.start.pomodoro.focus

import android.app.Activity
import android.view.WindowManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Focus Mode Manager - Handles screen dimming and keep-screen-on during focus sessions.
 * 
 * Features:
 * - Keep screen on during focus
 * - Dim screen slightly to reduce distractions
 * - Restore on pause/complete
 */

/**
 * Composable effect that keeps the screen on during focus sessions.
 * Also applies slight dimming for focus mode.
 */
@Composable
fun FocusModeEffect(
    isEnabled: Boolean,
    dimAmount: Float = 0.1f // Slight dim for focus (0.0 = no dim, 1.0 = fully dim)
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(isEnabled, lifecycleOwner) {
        val window = activity?.window

        if (isEnabled && window != null) {
            // Keep screen on
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            
            // Apply slight dimming
            val params = window.attributes
            params.screenBrightness = 1f - dimAmount // Slightly dimmed
            window.attributes = params
        }

        // Observe lifecycle for proper cleanup
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE && isEnabled) {
                // Could trigger a "come back" notification here
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            
            // Restore normal screen settings
            window?.let {
                it.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
                val params = it.attributes
                params.screenBrightness = WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE
                it.attributes = params
            }
        }
    }
}

/**
 * Composable overlay for focus mode - dims the edges of the screen
 */
@Composable
fun FocusModeOverlay(
    isEnabled: Boolean,
    content: @Composable () -> Unit
) {
    if (isEnabled) {
        androidx.compose.foundation.layout.Box {
            content()
            // Subtle vignette effect could be added here
        }
    } else {
        content()
    }
}
