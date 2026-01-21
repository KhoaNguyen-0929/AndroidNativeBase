package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel
import javax.inject.Inject

/**
 * Use case for updating the remaining time of a Pomodoro session.
 *
 * Validates that the new remaining time is within valid bounds (0 to durationSeconds).
 *
 * @param session The Pomodoro session to update
 * @param remainingSeconds The new remaining time in seconds
 * @return A new PomodoroSessionModel with updated remaining time
 * @throws IllegalArgumentException if remainingSeconds is out of valid range
 */
class UpdateRemainingTimeUseCase @Inject constructor() {
    operator fun invoke(session: PomodoroSessionModel, remainingSeconds: Int): PomodoroSessionModel {
        return session.updateRemaining(remainingSeconds)
    }
}
