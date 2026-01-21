package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel

/**
 * Use case for starting a Pomodoro session.
 *
 * Transitions the session from IDLE to RUNNING state.
 *
 * @param session The Pomodoro session to start
 * @return A new PomodoroSessionModel in RUNNING state
 * @throws IllegalArgumentException if session is not in IDLE state
 */
class StartPomodoroSessionUseCase {
    operator fun invoke(session: PomodoroSessionModel): PomodoroSessionModel {
        return session.start()
    }
}
