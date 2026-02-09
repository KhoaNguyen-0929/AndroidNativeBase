package vn.start.pomodoro.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Shared state holder for Pomodoro timer.
 * Enables synchronization between PomodoroForegroundService and PomodoroViewModel.
 *
 * This singleton maintains the current session state that can be observed by both
 * the foreground service and the ViewModel, ensuring UI stays in sync when the
 * app is backgrounded.
 */
@Singleton
class PomodoroStateHolder @Inject constructor() {

    private val _currentSession = MutableStateFlow<PomodoroSessionModel?>(null)
    val currentSession: StateFlow<PomodoroSessionModel?> = _currentSession.asStateFlow()

    private val _isServiceRunning = MutableStateFlow(false)
    val isServiceRunning: StateFlow<Boolean> = _isServiceRunning.asStateFlow()

    /**
     * Updates the current session state.
     * Called by the timer engine when session state changes.
     */
    fun updateSession(session: PomodoroSessionModel) {
        _currentSession.value = session
    }

    /**
     * Clears the current session.
     * Called when timer is stopped or session completes.
     */
    fun clearSession() {
        _currentSession.value = null
    }

    /**
     * Sets whether the foreground service is currently running.
     */
    fun setServiceRunning(running: Boolean) {
        _isServiceRunning.value = running
    }

    /**
     * Returns true if there's an active session (not completed).
     */
    fun hasActiveSession(): Boolean {
        val session = _currentSession.value ?: return false
        return session.status == PomodoroSessionStatus.RUNNING ||
               session.status == PomodoroSessionStatus.PAUSED
    }

    /**
     * Gets the current session type, or FOCUS as default.
     */
    fun getCurrentSessionType(): PomodoroSessionType {
        return _currentSession.value?.sessionType ?: PomodoroSessionType.FOCUS
    }

    /**
     * Gets the remaining seconds, or 0 if no session.
     */
    fun getRemainingSeconds(): Int {
        return _currentSession.value?.remainingSeconds ?: 0
    }
}
