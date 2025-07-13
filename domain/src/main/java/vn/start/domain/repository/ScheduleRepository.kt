package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.ScheduleModel

interface ScheduleRepository {
    fun getSchedulesForTask(taskId: Int): Flow<List<ScheduleModel>>
    suspend fun insertSchedule(schedule: ScheduleModel)
    suspend fun deleteSchedule(schedule: ScheduleModel)
}
