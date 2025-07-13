package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.PomodoroSessionModel

interface PomodoroSessionRepository {
    fun getSessionsForTask(taskId: Int): Flow<List<PomodoroSessionModel>>
    suspend fun insertSession(session: PomodoroSessionModel)
    suspend fun deleteSession(session: PomodoroSessionModel)
}
