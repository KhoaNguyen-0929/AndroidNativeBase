package vn.start.pomodoro.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.usecase.pomodoro.CompletePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.PausePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.ResumePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.StartPomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.UpdateRemainingTimeUseCase
import javax.inject.Inject

/**
 * Pomodoro Timer Engine.
 *
 * Orchestrates the Pomodoro timer logic using coroutines.
 * Maintains timer state and emits updates via StateFlow.
 *
 * The engine automatically stops when the session completes.
 */
class PomodoroTimerEngine @Inject constructor(
    private val startPomodoroSessionUseCase: StartPomodoroSessionUseCase,
    private val pausePomodoroSessionUseCase: PausePomodoroSessionUseCase,
    private val resumePomodoroSessionUseCase: ResumePomodoroSessionUseCase,
    private val updateRemainingTimeUseCase: UpdateRemainingTimeUseCase,
    private val completePomodoroSessionUseCase: CompletePomodoroSessionUseCase,
    private val clock: Clock
) {
    private val _sessionState = MutableStateFlow<PomodoroSessionModel?>(null)
    val sessionState: StateFlow<PomodoroSessionModel?> = _sessionState.asStateFlow()

    private var tickerJob: Job? = null

    /**
     * Initializes the timer engine with a Pomodoro session.
     *
     * @param session The initial Pomodoro session
     */
    fun initialize(session: PomodoroSessionModel) {
        _sessionState.value = session
    }

    /**
     * Starts the timer.
     *
     * Transitions the session to RUNNING state and begins ticking.
     */
    fun start(scope: CoroutineScope) {
        val currentSession = _sessionState.value ?: return

        if (currentSession.status != PomodoroSessionStatus.IDLE) return

        val startedSession = startPomodoroSessionUseCase(currentSession)
        _sessionState.value = startedSession

        startTicker(scope)
    }

    /**
     * Pauses the timer.
     *
     * Transitions the session to PAUSED state and stops ticking.
     */
    fun pause() {
        val currentSession = _sessionState.value ?: return

        if (currentSession.status != PomodoroSessionStatus.RUNNING) return

        val pausedSession = pausePomodoroSessionUseCase(currentSession)
        _sessionState.value = pausedSession

        stopTicker()
    }

    /**
     * Resumes the timer.
     *
     * Transitions the session from PAUSED to RUNNING state and resumes ticking.
     */
    fun resume(scope: CoroutineScope) {
        val currentSession = _sessionState.value ?: return

        if (currentSession.status != PomodoroSessionStatus.PAUSED) return

        val resumedSession = resumePomodoroSessionUseCase(currentSession)
        _sessionState.value = resumedSession

        startTicker(scope)
    }

    /**
     * Stops the timer.
     *
     * Stops ticking and clears the session state.
     */
    fun stop() {
        stopTicker()
        _sessionState.value = null
    }

    private fun startTicker(scope: CoroutineScope) {
        stopTicker()

        tickerJob = scope.launch {
            while (true) {
                delay(1000)
                onTick()
            }
        }
    }

    private fun stopTicker() {
        tickerJob?.cancel()
        tickerJob = null
    }

    private fun onTick() {
        val currentSession = _sessionState.value ?: return

        when (currentSession.status) {
            PomodoroSessionStatus.RUNNING -> {
                val newRemaining = (currentSession.remainingSeconds - 1).coerceAtLeast(0)

                if (newRemaining == 0) {
                    val completedSession = completePomodoroSessionUseCase(currentSession)
                    _sessionState.value = completedSession
                    stopTicker()
                } else {
                    val updatedSession = updateRemainingTimeUseCase(currentSession, newRemaining)
                    _sessionState.value = updatedSession
                }
            }
            else -> {
                stopTicker()
            }
        }
    }
}
