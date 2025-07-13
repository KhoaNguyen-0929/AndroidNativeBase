package vn.start.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.data.local.database.dao.ScheduleDao
import vn.start.data.mapper.ScheduleMapper
import vn.start.domain.model.ScheduleModel
import vn.start.domain.repository.ScheduleRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleRepositoryImpl @Inject constructor(
    private val scheduleDao: ScheduleDao,
    private val scheduleMapper: ScheduleMapper
) : ScheduleRepository {

    override fun getSchedulesForTask(taskId: Int): Flow<List<ScheduleModel>> {
        return scheduleDao.getSchedulesForTask(taskId).map { list ->
            list.map { scheduleMapper.entityToModel(it) }
        }
    }

    override suspend fun insertSchedule(schedule: ScheduleModel) {
        return scheduleDao.insertSchedule(scheduleMapper.modelToEntity(schedule))
    }

    override suspend fun deleteSchedule(schedule: ScheduleModel) {
        return scheduleDao.deleteSchedule(scheduleMapper.modelToEntity(schedule))
    }
}
