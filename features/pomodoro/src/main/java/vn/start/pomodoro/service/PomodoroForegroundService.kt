package vn.start.pomodoro.service

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.start.common.timer.PomodoroTicker
import vn.start.common.timer.SystemClock
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.usecase.pomodoro.CompletePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.UpdateRemainingTimeUseCase
import vn.start.pomodoro.engine.PomodoroTimerEngine
import javax.inject.Inject

/**
 * Foreground service for running Pomodoro timer in background.
 *
 * Responsibilities:
 * - Runs timer engine in foreground
 * - Updates notification with current state
 * - Handles lifecycle (start/stop)
 *
 * Does NOT contain business logic - delegates to PomodoroTimerEngine.
 */
@AndroidEntryPoint
class PomodoroForegroundService : Service() {

    companion object {
        const val EXTRA_SESSION_TYPE = "session_type"
        const val EXTRA_DURATION_SECONDS = "duration_seconds"

        const val ACTION_START = "vn.start.pomodoro.ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "vn.start.pomodoro.ACTION_STOP_SERVICE"
    }

    @Inject
    lateinit var updateRemainingTimeUseCase: UpdateRemainingTimeUseCase

    @Inject
    lateinit var completePomodoroSessionUseCase: CompletePomodoroSessionUseCase

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private var timerEngine: PomodoroTimerEngine? = null
    private var notificationManager: PomodoroNotificationManager? = null
    private var ticker: PomodoroTicker? = null

    override fun onCreate() {
        super.onCreate()
        notificationManager = PomodoroNotificationManager(this)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val sessionType = intent.getStringExtra(EXTRA_SESSION_TYPE)
                    ?.let { PomodoroSessionType.valueOf(it) }
                    ?: PomodoroSessionType.FOCUS
                val duration = intent.getIntExtra(EXTRA_DURATION_SECONDS, 25 * 60)

                startTimer(sessionType, duration)
            }
            ACTION_STOP_SERVICE -> {
                stopTimer()
                stopSelf()
            }
            PomodoroNotificationManager.ACTION_PAUSE -> {
                timerEngine?.pause()
            }
            PomodoroNotificationManager.ACTION_RESUME -> {
                timerEngine?.resume()
            }
            PomodoroNotificationManager.ACTION_STOP -> {
                stopTimer()
                stopSelf()
            }
        }

        return START_NOT_STICKY
    }

    private fun startTimer(sessionType: PomodoroSessionType, durationSeconds: Int) {
        // Create initial session
        val session = PomodoroSessionModel(
            sessionType = sessionType,
            durationSeconds = durationSeconds
        )

        // Create ticker and engine
        ticker = PomodoroTicker(scope = serviceScope, tickIntervalMs = 1000L)
        timerEngine = PomodoroTimerEngine(
            initialSession = session,
            updateRemainingTimeUseCase = updateRemainingTimeUseCase,
            completePomodoroSessionUseCase = completePomodoroSessionUseCase,
            ticker = ticker!!,
            clock = SystemClock(),
            scope = serviceScope
        )

        // Start foreground with initial notification
        val notification = notificationManager!!.buildNotification(
            sessionType = sessionType,
            status = PomodoroSessionStatus.IDLE,
            remainingSeconds = durationSeconds
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            startForeground(
                PomodoroNotificationManager.NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(PomodoroNotificationManager.NOTIFICATION_ID, notification)
        }

        // Start the timer
        timerEngine?.start()

        // Collect session state and update notification
        serviceScope.launch {
            timerEngine?.session?.collectLatest { currentSession ->
                updateNotification(currentSession)

                // Handle completion
                if (currentSession.status == PomodoroSessionStatus.COMPLETED) {
                    onSessionComplete(currentSession.sessionType)
                }
            }
        }
    }

    private fun updateNotification(session: PomodoroSessionModel) {
        val notification = notificationManager!!.buildNotification(
            sessionType = session.sessionType,
            status = session.status,
            remainingSeconds = session.remainingSeconds
        )
        notificationManager!!.notify(notification)
    }

    private fun onSessionComplete(sessionType: PomodoroSessionType) {
        // Show completion notification with sound/vibration
        val completionNotification = notificationManager!!.buildCompletionNotification(sessionType)
        notificationManager!!.notify(completionNotification)

        // Stop the service after a short delay
        stopSelf()
    }

    private fun stopTimer() {
        timerEngine?.stop()
        ticker?.stop()
        notificationManager?.cancel()
    }

    override fun onDestroy() {
        stopTimer()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
