package vn.start.focus.ui

import vn.start.domain.model.ScheduleModel

/**
 * MVI Contract for the Focus feature.
 * Separates concerns into State, Events (user actions), and Effects (one-time events).
 */
object FocusContract {
    
    /**
     * Represents the current UI state of the Focus screen.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val schedules: List<ScheduleModel> = emptyList(),
        val currentTaskId: Int? = null
    ) {
        companion object {
            val Initial = UiState()
        }
    }
    
    /**
     * User-triggered events/intents.
     */
    sealed class UiEvent {
        data class InsertSchedule(
            val taskId: Int,
            val startTime: Long,
            val duration: Int
        ) : UiEvent()
        
        data class LoadSchedulesForTask(val taskId: Int) : UiEvent()
        
        data object ResetError : UiEvent()
    }
    
    /**
     * One-time side effects that should not be replayed on configuration changes.
     * Examples: navigation, snackbar, toast, etc.
     */
    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data class NavigateToTask(val taskId: Int) : UiEffect()
        data object ScheduleInserted : UiEffect()
    }
}
