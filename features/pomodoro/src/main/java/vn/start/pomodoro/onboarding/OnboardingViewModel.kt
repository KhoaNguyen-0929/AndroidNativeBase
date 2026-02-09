package vn.start.pomodoro.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import vn.start.pomodoro.settings.PomodoroPreferences
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val preferences: PomodoroPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(OnboardingContract.UiState.Initial)
    val uiState: StateFlow<OnboardingContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<OnboardingContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    fun onEvent(event: OnboardingContract.UiEvent) {
        when (event) {
            is OnboardingContract.UiEvent.NextPage -> nextPage()
            is OnboardingContract.UiEvent.PreviousPage -> previousPage()
            is OnboardingContract.UiEvent.GoToPage -> goToPage(event.page)
            is OnboardingContract.UiEvent.SelectDuration -> selectDuration(event.minutes)
            is OnboardingContract.UiEvent.GetStarted -> getStarted()
            is OnboardingContract.UiEvent.Skip -> skip()
        }
    }

    private fun nextPage() {
        _uiState.update { state ->
            if (state.currentPage < state.totalPages - 1) {
                state.copy(currentPage = state.currentPage + 1)
            } else {
                state
            }
        }
    }

    private fun previousPage() {
        _uiState.update { state ->
            if (state.currentPage > 0) {
                state.copy(currentPage = state.currentPage - 1)
            } else {
                state
            }
        }
    }

    private fun goToPage(page: Int) {
        _uiState.update { it.copy(currentPage = page.coerceIn(0, it.totalPages - 1)) }
    }

    private fun selectDuration(minutes: Int) {
        _uiState.update { it.copy(selectedDurationMinutes = minutes) }
    }

    private fun getStarted() {
        viewModelScope.launch {
            // Save selected duration as default focus duration
            preferences.setFocusDuration(_uiState.value.selectedDurationMinutes)
            // Mark onboarding as complete
            preferences.setOnboardingComplete(true)
            _uiState.update { it.copy(isComplete = true) }
            _effects.send(OnboardingContract.UiEffect.NavigateToHome)
        }
    }

    private fun skip() {
        viewModelScope.launch {
            preferences.setOnboardingComplete(true)
            _effects.send(OnboardingContract.UiEffect.NavigateToHome)
        }
    }
}
