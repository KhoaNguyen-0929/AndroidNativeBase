package vn.start.data.local.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import vn.start.data.local.database.entities.PomodoroSession

@Dao
interface PomodoroSessionDao {
    @Query("SELECT * FROM pomodoro_sessions WHERE taskId = :taskId")
    fun getSessionsForTask(taskId: Int): Flow<List<PomodoroSession>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: PomodoroSession)

    @Delete
    suspend fun deleteSession(session: PomodoroSession)
}
