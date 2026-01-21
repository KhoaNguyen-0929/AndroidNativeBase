package vn.start.pomodoro.engine

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import vn.start.common.timer.Clock
import vn.start.common.timer.PomodoroTicker
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.usecase.pomodoro.CompletePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.UpdateRemainingTimeUseCase

/**
 * Engine that orchestrates a Pomodoro timer session.
 *
 * Responsibilities:
 * - Exposes session state as StateFlow
 * - On each tick: decrements remainingSeconds via use case
 * - Auto-completes session when remainingSeconds reaches 0
 * - Delegates state transitions to domain model
 *
 * Does NOT duplicate domain logic - uses domain model's state transitions.
 *
 * @param initialSession The initial Pomodoro session to manage
 * @param updateRemainingTimeUseCase Use case to update remaining time
 * @param completePomodoroSessionUseCase Use case to complete the session
 * @param ticker Ticker that emits time ticks
 * @param clock Clock abstraction for time
 * @param scope CoroutineScope for collecting ticks
 */
class PomodoroTimerEngine(
    initialSession: PomodoroSessionModel,
    private val updateRemainingTimeUseCase: UpdateRemainingTimeUseCase,
    private val completePomodoroSessionUseCase: CompletePomodoroSessionUseCase,
    private val ticker: PomodoroTicker,
    private val clock: Clock,
    private val scope: CoroutineScope
) {
    private val _session = MutableStateFlow(initialSession)

    /**
     * The current session state as a StateFlow.
     * Observers can collect this to react to session changes.
     */
    val session: StateFlow<PomodoroSessionModel> = _session.asStateFlow()

    init {
        // Collect ticks and update session
        scope.launch {
            ticker.ticks.collect {
                onTick()
            }
        }
    }

    /**
     * Starts the timer.
     * Transitions session from IDLE to RUNNING and starts the ticker.
     *
     * @throws IllegalArgumentException if session is not in IDLE state
     */
    fun start() {
        val currentSession = _session.value
        if (currentSession.isIdle) {
            _session.value = currentSession.start()
            ticker.start()
        }
    }

    /**
     * Pauses the timer.
     * Transitions session from RUNNING to PAUSED and stops the ticker.
     *
     * @throws IllegalArgumentException if session is not in RUNNING state
     */
    fun pause() {
        val currentSession = _session.value
        if (currentSession.isRunning) {
            _session.value = currentSession.pause()
            ticker.stop()
        }
    }

    /**
     * Resumes the timer.
     * Transitions session from PAUSED to RUNNING and starts the ticker.
     *
     * @throws IllegalArgumentException if session is not in PAUSED state
     */
    fun resume() {
        val currentSession = _session.value
        if (currentSession.isPaused) {
            _session.value = currentSession.resume()
            ticker.start()
        }
    }

    /**
     * Stops the timer completely.
     * Stops the ticker without changing session state.
     */
    fun stop() {
        ticker.stop()
    }

    /**
     * Returns the current remaining time in seconds.
     */
    val remainingSeconds: Int
        get() = _session.value.remainingSeconds

    /**
     * Returns true if the timer is currently running.
     */
    val isRunning: Boolean
        get() = _session.value.isRunning

    /**
     * Called on each tick from the ticker.
     * Decrements remaining time and auto-completes when reaching 0.
     */
    private fun onTick() {
        val currentSession = _session.value

        // Only process ticks when running
        if (!currentSession.isRunning) return

        val newRemaining = currentSession.remainingSeconds - 1

        if (newRemaining <= 0) {
            // Time's up - complete the session
            _session.value = completePomodoroSessionUseCase(currentSession)
            ticker.stop()
        } else {
            // Update remaining time
            _session.value = updateRemainingTimeUseCase(currentSession, newRemaining)
        }
    }
}
