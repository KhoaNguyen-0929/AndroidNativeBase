package vn.start.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.data.local.database.dao.PomodoroSessionDao
import vn.start.data.mapper.PomodoroSessionMapper
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.repository.PomodoroSessionRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PomodoroSessionRepositoryImpl @Inject constructor(
    private val pomodoroSessionDao: PomodoroSessionDao,
    private val pomodoroSessionMapper: PomodoroSessionMapper
) : PomodoroSessionRepository {

    override fun getSessionsForTask(taskId: Int): Flow<List<PomodoroSessionModel>> {
        return pomodoroSessionDao.getSessionsForTask(taskId).map { list ->
            list.map { pomodoroSessionMapper.fromEntity(it) }
        }
    }

    override suspend fun insertSession(session: PomodoroSessionModel) {
        return pomodoroSessionDao.insertSession(pomodoroSessionMapper.toEntity(session))
    }

    override suspend fun deleteSession(session: PomodoroSessionModel) {
        return pomodoroSessionDao.deleteSession(pomodoroSessionMapper.toEntity(session))
    }
}