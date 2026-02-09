package vn.start.pomodoro.settings

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
import javax.inject.Inject

@HiltViewModel
class PomodoroSettingsViewModel @Inject constructor(
    private val preferences: PomodoroPreferences
) : ViewModel() {

    private val _uiState = MutableStateFlow(PomodoroSettingsContract.UiState.Initial)
    val uiState: StateFlow<PomodoroSettingsContract.UiState> = _uiState.asStateFlow()

    private val _effects = Channel<PomodoroSettingsContract.UiEffect>(Channel.BUFFERED)
    val effects = _effects.receiveAsFlow()

    init {
        loadSettings()
    }

    private fun loadSettings() {
        viewModelScope.launch {
            preferences.settings.collect { settings ->
                _uiState.value = settings
            }
        }
    }

    fun onEvent(event: PomodoroSettingsContract.UiEvent) {
        viewModelScope.launch {
            when (event) {
                is PomodoroSettingsContract.UiEvent.SetFocusDuration -> {
                    preferences.setFocusDuration(event.minutes)
                    _uiState.update { it.copy(focusDurationMinutes = event.minutes) }
                }
                is PomodoroSettingsContract.UiEvent.SetShortBreakDuration -> {
                    preferences.setShortBreakDuration(event.minutes)
                    _uiState.update { it.copy(shortBreakDurationMinutes = event.minutes) }
                }
                is PomodoroSettingsContract.UiEvent.SetLongBreakDuration -> {
                    preferences.setLongBreakDuration(event.minutes)
                    _uiState.update { it.copy(longBreakDurationMinutes = event.minutes) }
                }
                is PomodoroSettingsContract.UiEvent.SetSessionsUntilLongBreak -> {
                    preferences.setSessionsUntilLongBreak(event.count)
                    _uiState.update { it.copy(sessionsUntilLongBreak = event.count) }
                }
                is PomodoroSettingsContract.UiEvent.SetAutoStartBreaks -> {
                    preferences.setAutoStartBreaks(event.enabled)
                    _uiState.update { it.copy(autoStartBreaks = event.enabled) }
                }
                is PomodoroSettingsContract.UiEvent.SetAutoStartFocus -> {
                    preferences.setAutoStartFocus(event.enabled)
                    _uiState.update { it.copy(autoStartFocus = event.enabled) }
                }
                is PomodoroSettingsContract.UiEvent.SetSoundEnabled -> {
                    preferences.setSoundEnabled(event.enabled)
                    _uiState.update { it.copy(soundEnabled = event.enabled) }
                }
                is PomodoroSettingsContract.UiEvent.SetVibrationEnabled -> {
                    preferences.setVibrationEnabled(event.enabled)
                    _uiState.update { it.copy(vibrationEnabled = event.enabled) }
                }
                is PomodoroSettingsContract.UiEvent.SetFocusModeEnabled -> {
                    preferences.setFocusModeEnabled(event.enabled)
                    _uiState.update { it.copy(focusModeEnabled = event.enabled) }
                }
                is PomodoroSettingsContract.UiEvent.ResetToDefaults -> {
                    preferences.resetToDefaults()
                    _uiState.value = PomodoroSettingsContract.UiState.Initial
                    _effects.send(PomodoroSettingsContract.UiEffect.ShowSnackbar("Settings reset to defaults"))
                }
                is PomodoroSettingsContract.UiEvent.NavigateBack -> {
                    _effects.send(PomodoroSettingsContract.UiEffect.NavigateBack)
                }
            }
        }
    }
}
