package vn.start.pomodoro.ui

import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType

/**
 * UI state for the Pomodoro screen.
 *
 * Represents all UI-relevant state in an immutable data class.
 * No business logic - pure data representation for the UI layer.
 */
data class PomodoroUiState(
    val sessionType: PomodoroSessionType = PomodoroSessionType.FOCUS,
    val status: PomodoroSessionStatus = PomodoroSessionStatus.IDLE,
    val remainingSeconds: Int = 0,
    val totalSeconds: Int = 0,
    val progress: Float = 0f,
    val taskId: Int? = null
) {
    /**
     * Formatted time string (MM:SS).
     */
    val formattedTime: String
        get() {
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            return "%02d:%02d".format(minutes, seconds)
        }

    /**
     * True if the timer is currently idle (not started).
     */
    val isIdle: Boolean
        get() = status == PomodoroSessionStatus.IDLE

    /**
     * True if the timer is currently running.
     */
    val isRunning: Boolean
        get() = status == PomodoroSessionStatus.RUNNING

    /**
     * True if the timer is currently paused.
     */
    val isPaused: Boolean
        get() = status == PomodoroSessionStatus.PAUSED

    /**
     * True if the session has been completed.
     */
    val isCompleted: Boolean
        get() = status == PomodoroSessionStatus.COMPLETED

    /**
     * True if the start button should be shown.
     */
    val showStartButton: Boolean
        get() = isIdle

    /**
     * True if the pause button should be shown.
     */
    val showPauseButton: Boolean
        get() = isRunning

    /**
     * True if the resume button should be shown.
     */
    val showResumeButton: Boolean
        get() = isPaused
}
