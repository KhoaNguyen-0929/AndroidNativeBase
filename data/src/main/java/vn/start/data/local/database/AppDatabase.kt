package vn.start.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import vn.start.data.local.database.converters.Converters
import vn.start.data.local.database.dao.PomodoroSessionDao
import vn.start.data.local.database.dao.ScheduleDao
import vn.start.data.local.database.dao.TaskDao
import vn.start.data.local.database.entities.PomodoroSession
import vn.start.data.local.database.entities.Schedule
import vn.start.data.local.database.entities.Task

@Database(
    entities = [Task::class, PomodoroSession::class, Schedule::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun pomodoroSessionDao(): PomodoroSessionDao
    abstract fun scheduleDao(): ScheduleDao
}