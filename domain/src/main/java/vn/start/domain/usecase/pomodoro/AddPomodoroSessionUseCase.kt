package vn.start.domain.usecase.pomodoro

import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.repository.PomodoroSessionRepository
import javax.inject.Inject

class AddPomodoroSessionUseCase @Inject constructor(
    private val pomodoroSessionRepository: PomodoroSessionRepository
) {
    suspend operator fun invoke(session: PomodoroSessionModel) = pomodoroSessionRepository.insertSession(session)
}