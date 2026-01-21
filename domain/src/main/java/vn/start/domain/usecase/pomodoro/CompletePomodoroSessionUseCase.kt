package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel
import javax.inject.Inject

/**
 * Use case for completing a Pomodoro session.
 *
 * Transitions the session from RUNNING or PAUSED to COMPLETED state.
 * Sets remaining seconds to 0 and records completion timestamp.
 *
 * @param session The Pomodoro session to complete
 * @return A new PomodoroSessionModel in COMPLETED state
 * @throws IllegalArgumentException if session is not in RUNNING or PAUSED state
 */
class CompletePomodoroSessionUseCase @Inject constructor() {
    operator fun invoke(session: PomodoroSessionModel): PomodoroSessionModel {
        return session.complete()
    }
}
