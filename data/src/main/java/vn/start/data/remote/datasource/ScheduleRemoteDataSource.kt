package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.remote.APIServices
import vn.start.data.remote.dto.ScheduleDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Schedule data.
 * Handles all schedule-related API calls.
 */
@Singleton
class ScheduleRemoteDataSource @Inject constructor(
    private val api: APIServices
) {

    /**
     * Get all schedules for a specific task.
     * @param taskId The ID of the task
     */
    fun getSchedulesForTask(taskId: Int): Flow<NetworkResult<List<ScheduleDto>>> {
        return safeApiFlow { api.getSchedulesForTask(taskId) }
    }

    /**
     * Create a new schedule.
     * @param schedule The schedule to create
     */
    fun createSchedule(schedule: ScheduleDto): Flow<NetworkResult<ScheduleDto>> {
        return safeApiFlow { api.createSchedule(schedule) }
    }

    /**
     * Update an existing schedule.
     * @param scheduleId The ID of the schedule to update
     * @param schedule The updated schedule data
     */
    fun updateSchedule(scheduleId: Int, schedule: ScheduleDto): Flow<NetworkResult<ScheduleDto>> {
        return safeApiFlow { api.updateSchedule(scheduleId, schedule) }
    }

    /**
     * Delete a schedule by ID.
     * @param scheduleId The ID of the schedule to delete
     */
    fun deleteSchedule(scheduleId: Int): Flow<NetworkResult<Unit>> {
        return safeApiFlow { api.deleteSchedule(scheduleId) }
    }
}
