package vn.start.pomodoro.history

import vn.start.domain.model.PomodoroSessionModel

/**
 * MVI Contract for Pomodoro History.
 */
object PomodoroHistoryContract {

    data class UiState(
        val isLoading: Boolean = false,
        val sessions: List<PomodoroSessionModel> = emptyList(),
        val todayCount: Int = 0,
        val weekCount: Int = 0,
        val totalFocusMinutes: Int = 0,
        val error: String? = null
    ) {
        companion object {
            val Initial = UiState(isLoading = true)
        }
    }

    sealed class UiEvent {
        data object LoadHistory : UiEvent()
        data object Refresh : UiEvent()
        data class DeleteSession(val session: PomodoroSessionModel) : UiEvent()
        data object NavigateBack : UiEvent()
    }

    sealed class UiEffect {
        data object NavigateBack : UiEffect()
        data class ShowSnackbar(val message: String) : UiEffect()
    }
}
