package vn.start.pomodoro.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pomodoro_settings")

/**
 * DataStore-backed preferences for Pomodoro settings.
 */
@Singleton
class PomodoroPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private object Keys {
        val FOCUS_DURATION = intPreferencesKey("focus_duration_minutes")
        val SHORT_BREAK_DURATION = intPreferencesKey("short_break_duration_minutes")
        val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration_minutes")
        val SESSIONS_UNTIL_LONG_BREAK = intPreferencesKey("sessions_until_long_break")
        val AUTO_START_BREAKS = booleanPreferencesKey("auto_start_breaks")
        val AUTO_START_FOCUS = booleanPreferencesKey("auto_start_focus")
        val SOUND_ENABLED = booleanPreferencesKey("sound_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
        val FOCUS_MODE_ENABLED = booleanPreferencesKey("focus_mode_enabled")
        val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")
    }

    val settings: Flow<PomodoroSettingsContract.UiState> = context.dataStore.data.map { prefs ->
        PomodoroSettingsContract.UiState(
            focusDurationMinutes = prefs[Keys.FOCUS_DURATION] ?: 25,
            shortBreakDurationMinutes = prefs[Keys.SHORT_BREAK_DURATION] ?: 5,
            longBreakDurationMinutes = prefs[Keys.LONG_BREAK_DURATION] ?: 15,
            sessionsUntilLongBreak = prefs[Keys.SESSIONS_UNTIL_LONG_BREAK] ?: 4,
            autoStartBreaks = prefs[Keys.AUTO_START_BREAKS] ?: false,
            autoStartFocus = prefs[Keys.AUTO_START_FOCUS] ?: false,
            soundEnabled = prefs[Keys.SOUND_ENABLED] ?: true,
            vibrationEnabled = prefs[Keys.VIBRATION_ENABLED] ?: true,
            focusModeEnabled = prefs[Keys.FOCUS_MODE_ENABLED] ?: true
        )
    }

    val isOnboardingComplete: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.ONBOARDING_COMPLETE] ?: false
    }

    suspend fun setOnboardingComplete(complete: Boolean) {
        context.dataStore.edit { it[Keys.ONBOARDING_COMPLETE] = complete }
    }

    suspend fun setFocusDuration(minutes: Int) {
        context.dataStore.edit { it[Keys.FOCUS_DURATION] = minutes }
    }

    suspend fun setShortBreakDuration(minutes: Int) {
        context.dataStore.edit { it[Keys.SHORT_BREAK_DURATION] = minutes }
    }

    suspend fun setLongBreakDuration(minutes: Int) {
        context.dataStore.edit { it[Keys.LONG_BREAK_DURATION] = minutes }
    }

    suspend fun setSessionsUntilLongBreak(count: Int) {
        context.dataStore.edit { it[Keys.SESSIONS_UNTIL_LONG_BREAK] = count }
    }

    suspend fun setAutoStartBreaks(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_START_BREAKS] = enabled }
    }

    suspend fun setAutoStartFocus(enabled: Boolean) {
        context.dataStore.edit { it[Keys.AUTO_START_FOCUS] = enabled }
    }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.SOUND_ENABLED] = enabled }
    }

    suspend fun setVibrationEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.VIBRATION_ENABLED] = enabled }
    }

    suspend fun setFocusModeEnabled(enabled: Boolean) {
        context.dataStore.edit { it[Keys.FOCUS_MODE_ENABLED] = enabled }
    }

    suspend fun resetToDefaults() {
        context.dataStore.edit { it.clear() }
    }
}

