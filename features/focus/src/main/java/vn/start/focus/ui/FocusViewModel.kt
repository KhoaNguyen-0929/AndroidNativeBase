package vn.start.focus.ui

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
import vn.start.domain.usecase.schedule.GetSchedulesForTaskUseCase
import javax.inject.Inject

/**
 * ViewModel for the Focus feature.
 * Implements MVI pattern with Contract-based state management.
 * Handles focus/pomodoro timer related logic and schedule management.
 */
@HiltViewModel
class FocusViewModel @Inject constructor(
    private val addScheduleUseCase: AddScheduleUseCase,
    private val getSchedulesForTaskUseCase: GetSchedulesForTaskUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(FocusContract.UiState.Initial)
    val uiState: StateFlow<FocusContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<FocusContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    /**
     * Handle user events.
     */
    fun onEvent(event: FocusContract.UiEvent) {
        when (event) {
            is FocusContract.UiEvent.InsertSchedule -> insertSchedule(
                event.taskId,
                event.startTime,
                event.duration
            )
            is FocusContract.UiEvent.LoadSchedulesForTask -> loadSchedulesForTask(event.taskId)
            is FocusContract.UiEvent.ResetError -> resetError()
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
                _effects.send(FocusContract.UiEffect.ScheduleInserted)
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        isLoading = false, 
                        error = e.message ?: "Unknown error occurred"
                    ) 
                }
                _effects.send(FocusContract.UiEffect.ShowSnackbar(e.message ?: "Error"))
            }
        }
    }

    private fun loadSchedulesForTask(taskId: Int) {
        _uiState.update { it.copy(currentTaskId = taskId) }
    }

    /**
     * Get all schedules for a specific task as a Flow.
     */
    fun getSchedulesForTask(taskId: Int) = getSchedulesForTaskUseCase(taskId)

    private fun resetError() {
        _uiState.update { it.copy(error = null) }
    }
}