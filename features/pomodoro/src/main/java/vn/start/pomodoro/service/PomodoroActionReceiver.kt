package vn.start.pomodoro.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint

/**
 * BroadcastReceiver for handling Pomodoro notification actions.
 *
 * Receives actions from notification buttons and forwards them
 * to PomodoroForegroundService.
 *
 * Does NOT contain business logic - only routes intents.
 */
@AndroidEntryPoint
class PomodoroActionReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val serviceIntent = Intent(context, PomodoroForegroundService::class.java).apply {
            action = intent.action
        }

        context.startService(serviceIntent)
    }
}
