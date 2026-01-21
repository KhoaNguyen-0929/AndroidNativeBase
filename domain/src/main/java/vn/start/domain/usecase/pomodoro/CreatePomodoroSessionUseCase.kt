package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.model.PomodoroSessionStatus

/**
 * Use case for creating a new Pomodoro session.
 *
 * @return A new PomodoroSessionModel in IDLE state
 */
class CreatePomodoroSessionUseCase {
    operator fun invoke(
        sessionType: PomodoroSessionType,
        durationSeconds: Int,
        taskId: Int? = null
    ): PomodoroSessionModel {
        return PomodoroSessionModel(
            sessionType = sessionType,
            durationSeconds = durationSeconds,
            status = PomodoroSessionStatus.IDLE,
            taskId = taskId
        )
    }
}
