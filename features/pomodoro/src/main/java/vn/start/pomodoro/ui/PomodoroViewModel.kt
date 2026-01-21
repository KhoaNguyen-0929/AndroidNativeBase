package vn.start.pomodoro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import vn.start.common.timer.Clock
import vn.start.common.timer.PomodoroTicker
import vn.start.common.timer.SystemClock
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.usecase.pomodoro.CompletePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.UpdateRemainingTimeUseCase
import vn.start.pomodoro.engine.PomodoroTimerEngine
import javax.inject.Inject

/**
 * ViewModel for the Pomodoro timer screen.
 *
 * Responsibilities:
 * - Exposes UI state as StateFlow
 * - Maps PomodoroSessionModel → PomodoroUiState
 * - Delegates timer actions to PomodoroTimerEngine
 *
 * Does NOT contain timer logic or business rules.
 */
@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val updateRemainingTimeUseCase: UpdateRemainingTimeUseCase,
    private val completePomodoroSessionUseCase: CompletePomodoroSessionUseCase
) : ViewModel() {

    // Default focus session duration: 25 minutes
    private val defaultDurationSeconds = 25 * 60

    // Clock abstraction for time
    private val clock: Clock = SystemClock()

    // Ticker for emitting time ticks
    private val ticker: PomodoroTicker = PomodoroTicker(
        scope = viewModelScope,
        tickIntervalMs = 1000L
    )

    // Timer engine that manages the session
    private val timerEngine: PomodoroTimerEngine = PomodoroTimerEngine(
        initialSession = createDefaultSession(),
        updateRemainingTimeUseCase = updateRemainingTimeUseCase,
        completePomodoroSessionUseCase = completePomodoroSessionUseCase,
        ticker = ticker,
        clock = clock,
        scope = viewModelScope
    )

    /**
     * UI state exposed to the Compose UI.
     * Maps PomodoroSessionModel to PomodoroUiState.
     */
    val uiState: StateFlow<PomodoroUiState> = timerEngine.session
        .map { session -> session.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PomodoroUiState()
        )

    /**
     * Starts the timer.
     * Only works when timer is in IDLE state.
     */
    fun start() {
        timerEngine.start()
    }

    /**
     * Pauses the timer.
     * Only works when timer is in RUNNING state.
     */
    fun pause() {
        timerEngine.pause()
    }

    /**
     * Resumes the timer.
     * Only works when timer is in PAUSED state.
     */
    fun resume() {
        timerEngine.resume()
    }

    /**
     * Stops the timer completely.
     */
    fun stop() {
        timerEngine.stop()
    }

    /**
     * Creates a default focus session.
     */
    private fun createDefaultSession(): PomodoroSessionModel {
        return PomodoroSessionModel(
            sessionType = PomodoroSessionType.FOCUS,
            durationSeconds = defaultDurationSeconds
        )
    }

    /**
     * Maps PomodoroSessionModel to PomodoroUiState.
     */
    private fun PomodoroSessionModel.toUiState(): PomodoroUiState {
        return PomodoroUiState(
            sessionType = sessionType,
            status = status,
            remainingSeconds = remainingSeconds,
            totalSeconds = durationSeconds,
            progress = progress,
            taskId = taskId
        )
    }
}

