package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel

/**
 * Use case for resuming a paused Pomodoro session.
 *
 * Transitions the session from PAUSED to RUNNING state.
 *
 * @param session The Pomodoro session to resume
 * @return A new PomodoroSessionModel in RUNNING state
 * @throws IllegalArgumentException if session is not in PAUSED state
 */
class ResumePomodoroSessionUseCase {
    operator fun invoke(session: PomodoroSessionModel): PomodoroSessionModel {
        return session.resume()
    }
}
