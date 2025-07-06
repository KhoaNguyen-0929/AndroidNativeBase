package vn.start.androidnativebase.ui.pomodoro

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

enum class SessionType {
    Focus, Break, LongBreak
}

data class PomodoroUiState(
    val timeLeft: String = "25:00",
    val isRunning: Boolean = false,
    val pomodoroCount: Int = 0,
    val currentSession: SessionType = SessionType.Focus,
    val totalSeconds: Int = 25 * 60, // 25 minutes in seconds
    val remainingSeconds: Int = 25 * 60,
    val progress: Float = 1.0f
)

class PomodoroViewModel : ViewModel() {
    
    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()
    
    private var timerJob: Job? = null
    
    fun startTimer() {
        if (_uiState.value.isRunning) return
        
        _uiState.value = _uiState.value.copy(isRunning = true)
        
        timerJob = viewModelScope.launch {
            while (_uiState.value.isRunning && _uiState.value.remainingSeconds > 0) {
                delay(1000) // 1 second delay
                
                val currentState = _uiState.value
                val newRemainingSeconds = currentState.remainingSeconds - 1
                
                if (newRemainingSeconds <= 0) {
                    // Timer finished
                    onTimerFinished()
                    break
                } else {
                    // Update timer
                    val progress = newRemainingSeconds.toFloat() / currentState.totalSeconds
                    val timeLeft = formatTime(newRemainingSeconds)
                    
                    _uiState.value = currentState.copy(
                        remainingSeconds = newRemainingSeconds,
                        timeLeft = timeLeft,
                        progress = progress
                    )
                }
            }
        }
    }
    
    fun pauseTimer() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        timerJob?.cancel()
    }
    
    fun resetTimer() {
        timerJob?.cancel()
        val currentState = _uiState.value
        val totalSeconds = getSessionDuration(currentState.currentSession)
        
        _uiState.value = PomodoroUiState(
            timeLeft = formatTime(totalSeconds),
            isRunning = false,
            pomodoroCount = currentState.pomodoroCount,
            currentSession = currentState.currentSession,
            totalSeconds = totalSeconds,
            remainingSeconds = totalSeconds,
            progress = 1.0f
        )
    }
    
    fun skipTimer() {
        timerJob?.cancel()
        
        val currentState = _uiState.value
        when (currentState.currentSession) {
            SessionType.Focus -> {
                // Move to break and increment pomodoro count
                val breakSeconds = 5 * 60
                _uiState.value = currentState.copy(
                    currentSession = SessionType.Break,
                    timeLeft = formatTime(breakSeconds),
                    totalSeconds = breakSeconds,
                    remainingSeconds = breakSeconds,
                    progress = 1.0f,
                    pomodoroCount = currentState.pomodoroCount + 1,
                    isRunning = false
                )
            }
            SessionType.Break -> {
                // Check if we should go to long break (every 4 pomodoros)
                val shouldLongBreak = (currentState.pomodoroCount + 1) % 4 == 0
                val nextSession = if (shouldLongBreak) SessionType.LongBreak else SessionType.Focus
                val nextSeconds = getSessionDuration(nextSession)
                
                _uiState.value = currentState.copy(
                    currentSession = nextSession,
                    timeLeft = formatTime(nextSeconds),
                    totalSeconds = nextSeconds,
                    remainingSeconds = nextSeconds,
                    progress = 1.0f,
                    isRunning = false
                )
            }
            SessionType.LongBreak -> {
                // Move back to focus
                val focusSeconds = 25 * 60
                _uiState.value = currentState.copy(
                    currentSession = SessionType.Focus,
                    timeLeft = formatTime(focusSeconds),
                    totalSeconds = focusSeconds,
                    remainingSeconds = focusSeconds,
                    progress = 1.0f,
                    isRunning = false
                )
            }
        }
    }
    
    private fun onTimerFinished() {
        val currentState = _uiState.value
        when (currentState.currentSession) {
            SessionType.Focus -> {
                // Auto-start break
                val breakSeconds = 5 * 60
                _uiState.value = currentState.copy(
                    currentSession = SessionType.Break,
                    timeLeft = formatTime(breakSeconds),
                    totalSeconds = breakSeconds,
                    remainingSeconds = breakSeconds,
                    progress = 1.0f,
                    pomodoroCount = currentState.pomodoroCount + 1,
                    isRunning = false
                )
                // Auto-start the break timer
                startTimer()
            }
            SessionType.Break -> {
                // Check if we should go to long break
                val shouldLongBreak = currentState.pomodoroCount % 4 == 0
                val nextSession = if (shouldLongBreak) SessionType.LongBreak else SessionType.Focus
                val nextSeconds = getSessionDuration(nextSession)
                
                _uiState.value = currentState.copy(
                    currentSession = nextSession,
                    timeLeft = formatTime(nextSeconds),
                    totalSeconds = nextSeconds,
                    remainingSeconds = nextSeconds,
                    progress = 1.0f,
                    isRunning = false
                )
                // Auto-start the next timer
                startTimer()
            }
            SessionType.LongBreak -> {
                // Move back to focus
                val focusSeconds = 25 * 60
                _uiState.value = currentState.copy(
                    currentSession = SessionType.Focus,
                    timeLeft = formatTime(focusSeconds),
                    totalSeconds = focusSeconds,
                    remainingSeconds = focusSeconds,
                    progress = 1.0f,
                    isRunning = false
                )
                // Auto-start the focus timer
                startTimer()
            }
        }
    }
    
    private fun getSessionDuration(sessionType: SessionType): Int {
        return when (sessionType) {
            SessionType.Focus -> 25 * 60 // 25 minutes
            SessionType.Break -> 5 * 60  // 5 minutes
            SessionType.LongBreak -> 15 * 60 // 15 minutes
        }
    }
    
    private fun formatTime(seconds: Int): String {
        val minutes = TimeUnit.SECONDS.toMinutes(seconds.toLong())
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
    
    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
} 