package vn.start.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import vn.start.data.local.database.dao.DemoDao
import vn.start.model.DemoDto

@Database(
    entities = [DemoDto::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract  fun workoutDao(): DemoDao
}