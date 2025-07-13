package vn.start.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import vn.start.data.local.database.entities.Schedule

@Dao
interface ScheduleDao {
    @Query("SELECT * FROM schedules WHERE taskId = :taskId")
    fun getSchedulesForTask(taskId: Int): Flow<List<Schedule>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSchedule(schedule: Schedule)

    @Delete
    suspend fun deleteSchedule(schedule: Schedule)
}
