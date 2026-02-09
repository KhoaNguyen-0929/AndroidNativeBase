package vn.start.pomodoro.ui

import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType

/**
 * MVI Contract for the Pomodoro feature.
 * Separates concerns into State, Events (user actions), and Effects (one-time events).
 */
object PomodoroContract {

    /**
     * Represents the current UI state of the Pomodoro screen.
     */
    data class UiState(
        val sessionType: PomodoroSessionType = PomodoroSessionType.FOCUS,
        val status: PomodoroSessionStatus = PomodoroSessionStatus.IDLE,
        val remainingSeconds: Int = 0,
        val totalSeconds: Int = 0,
        val progress: Float = 0f,
        val taskId: Int? = null,
        val taskName: String? = null,
        
        // Cycle tracking
        val currentCycle: Int = 1,
        val totalCycles: Int = 4,
        val completedSessionsToday: Int = 0,
        
        // Settings
        val autoStartNext: Boolean = false,
        val isServiceRunning: Boolean = false
    ) {
        val formattedTime: String
            get() {
                val minutes = remainingSeconds / 60
                val seconds = remainingSeconds % 60
                return "%02d:%02d".format(minutes, seconds)
            }

        val isIdle: Boolean get() = status == PomodoroSessionStatus.IDLE
        val isRunning: Boolean get() = status == PomodoroSessionStatus.RUNNING
        val isPaused: Boolean get() = status == PomodoroSessionStatus.PAUSED
        val isCompleted: Boolean get() = status == PomodoroSessionStatus.COMPLETED

        val showStartButton: Boolean get() = isIdle
        val showPauseButton: Boolean get() = isRunning
        val showResumeButton: Boolean get() = isPaused
        
        // Next session type prediction
        val nextSessionType: PomodoroSessionType
            get() = when (sessionType) {
                PomodoroSessionType.FOCUS -> {
                    if (currentCycle >= totalCycles) PomodoroSessionType.LONG_BREAK
                    else PomodoroSessionType.SHORT_BREAK
                }
                PomodoroSessionType.SHORT_BREAK -> PomodoroSessionType.FOCUS
                PomodoroSessionType.LONG_BREAK -> PomodoroSessionType.FOCUS
            }

        // Cycle display text
        val cycleText: String
            get() = "$currentCycle / $totalCycles"

        companion object {
            val Initial = UiState(
                totalSeconds = 25 * 60,
                remainingSeconds = 25 * 60
            )
        }
    }

    /**
     * User-triggered events/intents.
     */
    sealed class UiEvent {
        data object Start : UiEvent()
        data object Pause : UiEvent()
        data object Resume : UiEvent()
        data object Stop : UiEvent()
        data object Reset : UiEvent()
        
        // Skip to next session
        data object Skip : UiEvent()
        
        // Start next session after completion
        data object StartNextSession : UiEvent()
        
        data class ChangeSessionType(val sessionType: PomodoroSessionType) : UiEvent()
        data class SetDuration(val minutes: Int) : UiEvent()
        data class LinkToTask(val taskId: Int?, val taskName: String?) : UiEvent()
        
        data object NavigateToSettings : UiEvent()
        data object NavigateToHistory : UiEvent()
    }

    /**
     * One-time side effects that should not be replayed on configuration changes.
     */
    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data object SessionCompleted : UiEffect()
        data object BreakCompleted : UiEffect()
        data class ShowCompletionDialog(val sessionType: PomodoroSessionType) : UiEffect()
        data object NavigateToSettings : UiEffect()
        data object NavigateToHistory : UiEffect()
        data class PlaySound(val soundType: SoundType) : UiEffect()
    }

    enum class SoundType {
        FOCUS_COMPLETE,
        BREAK_COMPLETE,
        TICK
    }
}
