package vn.start.data.local.database

import vn.start.data.local.database.dao.DemoDao

abstract class AppDatabase : RoomDatabase() {
    abstract  fun workoutDao(): DemoDao
}