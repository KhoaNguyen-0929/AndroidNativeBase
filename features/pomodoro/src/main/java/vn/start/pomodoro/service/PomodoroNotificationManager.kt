package vn.start.pomodoro.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.pomodoro.R

/**
 * Manages Pomodoro timer notifications.
 *
 * Responsibilities:
 * - Creates and manages notification channel
 * - Builds notifications for different timer states
 * - Provides action pending intents for Pause/Resume/Stop
 *
 * Does NOT contain timer or business logic.
 */
class PomodoroNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "pomodoro_timer"
        const val NOTIFICATION_ID = 1001

        // Action constants for BroadcastReceiver
        const val ACTION_PAUSE = "vn.start.pomodoro.ACTION_PAUSE"
        const val ACTION_RESUME = "vn.start.pomodoro.ACTION_RESUME"
        const val ACTION_STOP = "vn.start.pomodoro.ACTION_STOP"
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel()
    }

    /**
     * Creates the notification channel for Android O+.
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows ongoing Pomodoro timer status"
                setShowBadge(false)
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    /**
     * Builds a notification for the current timer state.
     *
     * @param sessionType The type of session (FOCUS, SHORT_BREAK, LONG_BREAK)
     * @param status The current session status
     * @param remainingSeconds Remaining time in seconds
     * @return The built notification
     */
    fun buildNotification(
        sessionType: PomodoroSessionType,
        status: PomodoroSessionStatus,
        remainingSeconds: Int
    ): Notification {
        val title = when (sessionType) {
            PomodoroSessionType.FOCUS -> "Focus Session"
            PomodoroSessionType.SHORT_BREAK -> "Short Break"
            PomodoroSessionType.LONG_BREAK -> "Long Break"
        }

        val timeText = formatTime(remainingSeconds)
        val contentText = when (status) {
            PomodoroSessionStatus.RUNNING -> timeText
            PomodoroSessionStatus.PAUSED -> "$timeText (Paused)"
            PomodoroSessionStatus.COMPLETED -> "Session Complete!"
            PomodoroSessionStatus.IDLE -> "Ready to start"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(contentText)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(status == PomodoroSessionStatus.RUNNING || status == PomodoroSessionStatus.PAUSED)
            .setOnlyAlertOnce(true)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)

        // Add content intent to open app
        val contentIntent = context.packageManager
            .getLaunchIntentForPackage(context.packageName)
            ?.let { intent ->
                PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            }
        contentIntent?.let { builder.setContentIntent(it) }

        // Add actions based on status
        when (status) {
            PomodoroSessionStatus.RUNNING -> {
                builder.addAction(
                    android.R.drawable.ic_media_pause,
                    "Pause",
                    createActionPendingIntent(ACTION_PAUSE)
                )
                builder.addAction(
                    android.R.drawable.ic_delete,
                    "Stop",
                    createActionPendingIntent(ACTION_STOP)
                )
            }
            PomodoroSessionStatus.PAUSED -> {
                builder.addAction(
                    android.R.drawable.ic_media_play,
                    "Resume",
                    createActionPendingIntent(ACTION_RESUME)
                )
                builder.addAction(
                    android.R.drawable.ic_delete,
                    "Stop",
                    createActionPendingIntent(ACTION_STOP)
                )
            }
            else -> {
                // No actions for COMPLETED or IDLE
            }
        }

        return builder.build()
    }

    /**
     * Builds a completion notification with alert.
     */
    fun buildCompletionNotification(sessionType: PomodoroSessionType): Notification {
        val title = when (sessionType) {
            PomodoroSessionType.FOCUS -> "Focus Session Complete! 🎉"
            PomodoroSessionType.SHORT_BREAK -> "Break Over!"
            PomodoroSessionType.LONG_BREAK -> "Long Break Over!"
        }

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setContentText("Time for the next session")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_SOUND or NotificationCompat.DEFAULT_VIBRATE)
            .build()
    }

    /**
     * Updates the notification.
     */
    fun notify(notification: Notification) {
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    /**
     * Cancels the notification.
     */
    fun cancel() {
        notificationManager.cancel(NOTIFICATION_ID)
    }

    private fun createActionPendingIntent(action: String): PendingIntent {
        val intent = Intent(action).apply {
            setPackage(context.packageName)
        }
        return PendingIntent.getBroadcast(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return "%02d:%02d".format(minutes, secs)
    }
}
