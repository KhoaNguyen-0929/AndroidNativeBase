package vn.start.dashboard.ui

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
import vn.start.domain.model.ScheduleModel
import vn.start.domain.usecase.schedule.AddScheduleUseCase
import vn.start.domain.usecase.task.GetAllTasksUseCase
import javax.inject.Inject

/**
 * ViewModel for the Dashboard feature.
 * Implements MVI pattern with Contract-based state management.
 * Handles dashboard data display and task/schedule management.
 */
@HiltViewModel
class DashBoardViewModel @Inject constructor(
    private val addScheduleUseCase: AddScheduleUseCase,
    private val getAllTasksUseCase: GetAllTasksUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardContract.UiState.Initial)
    val uiState: StateFlow<DashboardContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<DashboardContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    /**
     * Handle user events.
     */
    fun onEvent(event: DashboardContract.UiEvent) {
        when (event) {
            is DashboardContract.UiEvent.InsertSchedule -> insertSchedule(
                event.taskId,
                event.startTime,
                event.duration
            )
            is DashboardContract.UiEvent.LoadTasks -> loadTasks()
            is DashboardContract.UiEvent.ResetError -> resetError()
        }
    }

    private fun insertSchedule(taskId: Int, startTime: Long, duration: Int) {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val schedule = ScheduleModel(
                    id = 0,
                    taskId = taskId,
                    startTime = startTime,
                    endTime = startTime + duration
                )
                addScheduleUseCase(schedule)
                _uiState.update { it.copy(isLoading = false) }
                _effects.send(DashboardContract.UiEffect.ScheduleInserted)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
                _effects.send(DashboardContract.UiEffect.ShowSnackbar(e.message ?: "Error"))
            }
        }
    }

    private fun loadTasks() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true) }
            getAllTasksUseCase().collect { tasks ->
                _uiState.update { 
                    it.copy(isLoading = false, tasks = tasks) 
                }
            }
        }
    }

    /**
     * Get all tasks as a Flow.
     */
    fun getAllTasks() = getAllTasksUseCase()

    private fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}