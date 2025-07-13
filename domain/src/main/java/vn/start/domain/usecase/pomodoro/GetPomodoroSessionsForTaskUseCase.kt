package vn.start.domain.usecase.pomodoro

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.repository.PomodoroSessionRepository
import javax.inject.Inject

class GetPomodoroSessionsForTaskUseCase @Inject constructor(
    private val pomodoroSessionRepository: PomodoroSessionRepository
) {
    operator fun invoke(taskId: Int): Flow<List<PomodoroSessionModel>> = 
        pomodoroSessionRepository.getSessionsForTask(taskId)
}