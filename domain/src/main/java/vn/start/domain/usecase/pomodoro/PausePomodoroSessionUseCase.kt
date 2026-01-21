package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel

/**
 * Use case for pausing a Pomodoro session.
 *
 * Transitions the session from RUNNING to PAUSED state.
 *
 * @param session The Pomodoro session to pause
 * @return A new PomodoroSessionModel in PAUSED state
 * @throws IllegalArgumentException if session is not in RUNNING state
 */
class PausePomodoroSessionUseCase {
    operator fun invoke(session: PomodoroSessionModel): PomodoroSessionModel {
        return session.pause()
    }
}
