package vn.start.pomodoro.settings

/**
 * MVI Contract for Pomodoro Settings.
 */
object PomodoroSettingsContract {

    data class UiState(
        val focusDurationMinutes: Int = 25,
        val shortBreakDurationMinutes: Int = 5,
        val longBreakDurationMinutes: Int = 15,
        val sessionsUntilLongBreak: Int = 4,
        val autoStartBreaks: Boolean = false,
        val autoStartFocus: Boolean = false,
        val soundEnabled: Boolean = true,
        val vibrationEnabled: Boolean = true,
        val focusModeEnabled: Boolean = true
    ) {
        companion object {
            val Initial = UiState()
        }
    }

    sealed class UiEvent {
        data class SetFocusDuration(val minutes: Int) : UiEvent()
        data class SetShortBreakDuration(val minutes: Int) : UiEvent()
        data class SetLongBreakDuration(val minutes: Int) : UiEvent()
        data class SetSessionsUntilLongBreak(val count: Int) : UiEvent()
        data class SetAutoStartBreaks(val enabled: Boolean) : UiEvent()
        data class SetAutoStartFocus(val enabled: Boolean) : UiEvent()
        data class SetSoundEnabled(val enabled: Boolean) : UiEvent()
        data class SetVibrationEnabled(val enabled: Boolean) : UiEvent()
        data class SetFocusModeEnabled(val enabled: Boolean) : UiEvent()
        data object ResetToDefaults : UiEvent()
        data object NavigateBack : UiEvent()
    }

    sealed class UiEffect {
        data object NavigateBack : UiEffect()
        data class ShowSnackbar(val message: String) : UiEffect()
    }
}
