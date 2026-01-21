package vn.start.dashboard.ui

import vn.start.domain.model.TaskModel

/**
 * MVI Contract for the Dashboard feature.
 * Separates concerns into State, Events (user actions), and Effects (one-time events).
 */
object DashboardContract {
    
    /**
     * Represents the current UI state of the Dashboard screen.
     */
    data class UiState(
        val isLoading: Boolean = false,
        val error: String? = null,
        val tasks: List<TaskModel> = emptyList(),
        val todayScheduleCount: Int = 0
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
        
        data object LoadTasks : UiEvent()
        
        data object ResetError : UiEvent()
    }
    
    /**
     * One-time side effects that should not be replayed on configuration changes.
     */
    sealed class UiEffect {
        data class ShowSnackbar(val message: String) : UiEffect()
        data class NavigateToTask(val taskId: Int) : UiEffect()
        data object ScheduleInserted : UiEffect()
    }
}
