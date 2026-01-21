package vn.start.pomodoro.service

import android.content.Context
import android.content.Intent
import android.os.Build
import vn.start.domain.model.PomodoroSessionType

/**
 * Helper object for starting/stopping the Pomodoro foreground service.
 *
 * Provides a clean API for ViewModel/UI to interact with the service
 * without knowing Android service details.
 */
object PomodoroServiceController {

    /**
     * Starts the Pomodoro timer service with the given session configuration.
     *
     * @param context Application context
     * @param sessionType Type of session (FOCUS, SHORT_BREAK, LONG_BREAK)
     * @param durationSeconds Duration in seconds
     */
    fun startService(
        context: Context,
        sessionType: PomodoroSessionType,
        durationSeconds: Int
    ) {
        val intent = Intent(context, PomodoroForegroundService::class.java).apply {
            action = PomodoroForegroundService.ACTION_START
            putExtra(PomodoroForegroundService.EXTRA_SESSION_TYPE, sessionType.name)
            putExtra(PomodoroForegroundService.EXTRA_DURATION_SECONDS, durationSeconds)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    /**
     * Stops the Pomodoro timer service.
     *
     * @param context Application context
     */
    fun stopService(context: Context) {
        val intent = Intent(context, PomodoroForegroundService::class.java).apply {
            action = PomodoroForegroundService.ACTION_STOP_SERVICE
        }
        context.startService(intent)
    }

    /**
     * Sends a pause action to the service.
     */
    fun pauseTimer(context: Context) {
        val intent = Intent(context, PomodoroForegroundService::class.java).apply {
            action = PomodoroNotificationManager.ACTION_PAUSE
        }
        context.startService(intent)
    }

    /**
     * Sends a resume action to the service.
     */
    fun resumeTimer(context: Context) {
        val intent = Intent(context, PomodoroForegroundService::class.java).apply {
            action = PomodoroNotificationManager.ACTION_RESUME
        }
        context.startService(intent)
    }
}
