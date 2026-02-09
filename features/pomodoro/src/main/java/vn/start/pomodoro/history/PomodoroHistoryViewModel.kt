package vn.start.pomodoro.history

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
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.repository.PomodoroSessionRepository
import javax.inject.Inject

@HiltViewModel
class PomodoroHistoryViewModel @Inject constructor(
    private val repository: PomodoroSessionRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroHistoryContract.UiState.Initial)
    val uiState: StateFlow<PomodoroHistoryContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<PomodoroHistoryContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadHistory()
    }

    fun onEvent(event: PomodoroHistoryContract.UiEvent) {
        when (event) {
            is PomodoroHistoryContract.UiEvent.LoadHistory -> loadHistory()
            is PomodoroHistoryContract.UiEvent.Refresh -> loadHistory()
            is PomodoroHistoryContract.UiEvent.DeleteSession -> deleteSession(event.session)
            is PomodoroHistoryContract.UiEvent.NavigateBack -> navigateBack()
        }
    }

    private fun loadHistory() {
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                repository.getAllSessions().collect { sessions ->
                    val completedSessions = sessions.filter { 
                        it.status == PomodoroSessionStatus.COMPLETED 
                    }
                    val focusSessions = completedSessions.filter { 
                        it.sessionType == PomodoroSessionType.FOCUS 
                    }
                    
                    val now = System.currentTimeMillis()
                    val todayStart = now - (now % (24 * 60 * 60 * 1000))
                    val weekStart = todayStart - (7 * 24 * 60 * 60 * 1000)
                    
                    val todayCount = focusSessions.count { 
                        (it.completedAt ?: 0) >= todayStart 
                    }
                    val weekCount = focusSessions.count { 
                        (it.completedAt ?: 0) >= weekStart 
                    }
                    val totalMinutes = focusSessions.sumOf { it.durationSeconds } / 60

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            sessions = completedSessions.sortedByDescending { s -> s.completedAt },
                            todayCount = todayCount,
                            weekCount = weekCount,
                            totalFocusMinutes = totalMinutes
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load history"
                    )
                }
            }
        }
    }

    private fun deleteSession(session: PomodoroSessionModel) {
        viewModelScope.launch(ioDispatcher) {
            try {
                repository.deleteSession(session)
                _effects.send(PomodoroHistoryContract.UiEffect.ShowSnackbar("Session deleted"))
            } catch (e: Exception) {
                _effects.send(PomodoroHistoryContract.UiEffect.ShowSnackbar("Failed to delete"))
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effects.send(PomodoroHistoryContract.UiEffect.NavigateBack)
        }
    }
}
