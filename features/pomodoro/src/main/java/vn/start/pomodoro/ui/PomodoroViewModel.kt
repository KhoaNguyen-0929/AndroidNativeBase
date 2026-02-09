package vn.start.pomodoro.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.start.common.di.IoDispatcher
import vn.start.common.timer.Clock
import vn.start.common.timer.PomodoroTicker
import vn.start.common.timer.SystemClock
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.usecase.pomodoro.AddPomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.CompletePomodoroSessionUseCase
import vn.start.domain.usecase.pomodoro.UpdateRemainingTimeUseCase
import vn.start.pomodoro.engine.PomodoroTimerEngine
import vn.start.pomodoro.settings.PomodoroPreferences
import javax.inject.Inject

/**
 * ViewModel for the Pomodoro timer screen.
 * Implements MVI pattern with Contract-based state management.
 *
 * Timer Logic:
 * - 25min Focus → 5min Short Break → (4 cycles) → 15min Long Break
 * - Auto-switch between sessions (configurable)
 * - Manual skip option
 */
@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val updateRemainingTimeUseCase: UpdateRemainingTimeUseCase,
    private val completePomodoroSessionUseCase: CompletePomodoroSessionUseCase,
    private val addPomodoroSessionUseCase: AddPomodoroSessionUseCase,
    private val preferences: PomodoroPreferences,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    // Session durations in seconds (loaded from preferences)
    private var focusDuration = 25 * 60
    private var shortBreakDuration = 5 * 60
    private var longBreakDuration = 15 * 60
    private var sessionsUntilLongBreak = 4
    private var autoStartNext = false

    // Clock and ticker
    private val clock: Clock = SystemClock()
    private val ticker: PomodoroTicker = PomodoroTicker(
        scope = viewModelScope,
        tickIntervalMs = 1000L
    )

    // UI State - must be initialized before timerEngine
    private val _uiState = MutableStateFlow(PomodoroContract.UiState.Initial.copy(
        totalSeconds = focusDuration,
        remainingSeconds = focusDuration
    ))
    val uiState: StateFlow<PomodoroContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<PomodoroContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    // Timer engine - initialized after _uiState
    private var timerEngine: PomodoroTimerEngine = createTimerEngine(
        PomodoroSessionType.FOCUS,
        focusDuration
    )

    init {
        loadSettings()
        observeTimerEngine()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            preferences.settings.collect { settings ->
                focusDuration = settings.focusDurationMinutes * 60
                shortBreakDuration = settings.shortBreakDurationMinutes * 60
                longBreakDuration = settings.longBreakDurationMinutes * 60
                sessionsUntilLongBreak = settings.sessionsUntilLongBreak
                autoStartNext = settings.autoStartBreaks || settings.autoStartFocus
                
                // Update state if idle
                if (_uiState.value.isIdle) {
                    val duration = getDurationForType(_uiState.value.sessionType)
                    _uiState.update { 
                        it.copy(
                            totalSeconds = duration,
                            remainingSeconds = duration,
                            totalCycles = sessionsUntilLongBreak,
                            autoStartNext = autoStartNext
                        ) 
                    }
                }
            }
        }
    }

    fun onEvent(event: PomodoroContract.UiEvent) {
        when (event) {
            is PomodoroContract.UiEvent.Start -> start()
            is PomodoroContract.UiEvent.Pause -> pause()
            is PomodoroContract.UiEvent.Resume -> resume()
            is PomodoroContract.UiEvent.Stop -> stop()
            is PomodoroContract.UiEvent.Reset -> reset()
            is PomodoroContract.UiEvent.Skip -> skip()
            is PomodoroContract.UiEvent.StartNextSession -> startNextSession()
            is PomodoroContract.UiEvent.ChangeSessionType -> changeSessionType(event.sessionType)
            is PomodoroContract.UiEvent.SetDuration -> setDuration(event.minutes)
            is PomodoroContract.UiEvent.LinkToTask -> linkToTask(event.taskId, event.taskName)
            is PomodoroContract.UiEvent.NavigateToSettings -> navigateToSettings()
            is PomodoroContract.UiEvent.NavigateToHistory -> navigateToHistory()
        }
    }

    private fun start() {
        if (_uiState.value.isIdle || _uiState.value.isCompleted) {
            // If completed, reset for new session
            if (_uiState.value.isCompleted) {
                val duration = getDurationForType(_uiState.value.sessionType)
                timerEngine = createTimerEngine(_uiState.value.sessionType, duration)
                observeTimerEngine()
            }
            timerEngine.start()
        }
    }

    private fun pause() {
        if (_uiState.value.isRunning) {
            timerEngine.pause()
        }
    }

    private fun resume() {
        if (_uiState.value.isPaused) {
            timerEngine.resume()
        }
    }

    private fun stop() {
        timerEngine.stop()
        val duration = getDurationForType(_uiState.value.sessionType)
        timerEngine = createTimerEngine(_uiState.value.sessionType, duration)
        observeTimerEngine()
        _uiState.update { 
            it.copy(
                status = PomodoroSessionStatus.IDLE,
                remainingSeconds = duration,
                progress = 0f
            ) 
        }
    }

    private fun reset() {
        stop()
        _uiState.update { 
            PomodoroContract.UiState.Initial.copy(
                totalSeconds = focusDuration,
                remainingSeconds = focusDuration,
                totalCycles = sessionsUntilLongBreak
            )
        }
    }

    /**
     * Skip current session and move to next
     */
    private fun skip() {
        timerEngine.stop()
        transitionToNextSession()
    }

    /**
     * Start the next session after completion dialog
     */
    private fun startNextSession() {
        transitionToNextSession()
        // Auto-start if enabled
        if (autoStartNext) {
            start()
        }
    }

    /**
     * Transition to the next session based on cycle logic
     */
    private fun transitionToNextSession() {
        val currentState = _uiState.value
        val nextType = currentState.nextSessionType
        var newCycle = currentState.currentCycle
        
        // Update cycle count when completing a focus session
        if (currentState.sessionType == PomodoroSessionType.FOCUS) {
            // After focus, we go to break (cycle stays same until long break completes)
        } else if (currentState.sessionType == PomodoroSessionType.LONG_BREAK) {
            // After long break, reset to cycle 1
            newCycle = 1
        } else if (currentState.sessionType == PomodoroSessionType.SHORT_BREAK) {
            // After short break, increment cycle
            newCycle = currentState.currentCycle + 1
        }

        val duration = getDurationForType(nextType)
        timerEngine = createTimerEngine(nextType, duration)
        observeTimerEngine()
        
        _uiState.update { 
            it.copy(
                sessionType = nextType,
                status = PomodoroSessionStatus.IDLE,
                totalSeconds = duration,
                remainingSeconds = duration,
                progress = 0f,
                currentCycle = newCycle
            ) 
        }
    }

    private fun changeSessionType(sessionType: PomodoroSessionType) {
        if (_uiState.value.isIdle) {
            val duration = getDurationForType(sessionType)
            timerEngine.stop()
            timerEngine = createTimerEngine(sessionType, duration)
            observeTimerEngine()
            _uiState.update {
                it.copy(
                    sessionType = sessionType,
                    status = PomodoroSessionStatus.IDLE,
                    totalSeconds = duration,
                    remainingSeconds = duration,
                    progress = 0f
                )
            }
        }
    }

    private fun setDuration(minutes: Int) {
        val seconds = minutes * 60
        when (_uiState.value.sessionType) {
            PomodoroSessionType.FOCUS -> focusDuration = seconds
            PomodoroSessionType.SHORT_BREAK -> shortBreakDuration = seconds
            PomodoroSessionType.LONG_BREAK -> longBreakDuration = seconds
        }
        if (_uiState.value.isIdle) {
            timerEngine = createTimerEngine(_uiState.value.sessionType, seconds)
            observeTimerEngine()
            _uiState.update {
                it.copy(totalSeconds = seconds, remainingSeconds = seconds)
            }
        }
    }

    private fun linkToTask(taskId: Int?, taskName: String?) {
        _uiState.update { it.copy(taskId = taskId, taskName = taskName) }
    }

    private fun navigateToSettings() {
        viewModelScope.launch {
            _effects.send(PomodoroContract.UiEffect.NavigateToSettings)
        }
    }

    private fun navigateToHistory() {
        viewModelScope.launch {
            _effects.send(PomodoroContract.UiEffect.NavigateToHistory)
        }
    }

    private fun getDurationForType(sessionType: PomodoroSessionType): Int {
        return when (sessionType) {
            PomodoroSessionType.FOCUS -> focusDuration
            PomodoroSessionType.SHORT_BREAK -> shortBreakDuration
            PomodoroSessionType.LONG_BREAK -> longBreakDuration
        }
    }

    private fun createTimerEngine(
        sessionType: PomodoroSessionType,
        durationSeconds: Int
    ): PomodoroTimerEngine {
        val session = PomodoroSessionModel(
            sessionType = sessionType,
            durationSeconds = durationSeconds,
            taskId = _uiState.value.taskId
        )
        return PomodoroTimerEngine(
            initialSession = session,
            updateRemainingTimeUseCase = updateRemainingTimeUseCase,
            completePomodoroSessionUseCase = completePomodoroSessionUseCase,
            ticker = ticker,
            clock = clock,
            scope = viewModelScope
        )
    }

    private fun observeTimerEngine() {
        viewModelScope.launch {
            timerEngine.session.collect { session ->
                _uiState.update {
                    it.copy(
                        sessionType = session.sessionType,
                        status = session.status,
                        remainingSeconds = session.remainingSeconds,
                        totalSeconds = session.durationSeconds,
                        progress = session.progress,
                        taskId = session.taskId
                    )
                }

                // Handle completion
                if (session.isCompleted) {
                    onSessionComplete(session)
                }
            }
        }
    }

    private fun onSessionComplete(session: PomodoroSessionModel) {
        viewModelScope.launch(ioDispatcher) {
            // Save completed session to database
            try {
                addPomodoroSessionUseCase(session)
            } catch (e: Exception) {
                _effects.send(PomodoroContract.UiEffect.ShowSnackbar("Failed to save session"))
            }

            // Update completed count for focus sessions
            if (session.sessionType == PomodoroSessionType.FOCUS) {
                _uiState.update { 
                    it.copy(completedSessionsToday = it.completedSessionsToday + 1) 
                }
            }

            // Send completion effect
            val effect = when (session.sessionType) {
                PomodoroSessionType.FOCUS -> PomodoroContract.UiEffect.SessionCompleted
                else -> PomodoroContract.UiEffect.BreakCompleted
            }
            _effects.send(effect)

            // Play completion sound
            val soundType = when (session.sessionType) {
                PomodoroSessionType.FOCUS -> PomodoroContract.SoundType.FOCUS_COMPLETE
                else -> PomodoroContract.SoundType.BREAK_COMPLETE
            }
            _effects.send(PomodoroContract.UiEffect.PlaySound(soundType))

            // Show completion dialog
            _effects.send(PomodoroContract.UiEffect.ShowCompletionDialog(session.sessionType))

            // Auto-start next session if enabled
            if (autoStartNext) {
                transitionToNextSession()
                start()
            }
        }
    }
}
