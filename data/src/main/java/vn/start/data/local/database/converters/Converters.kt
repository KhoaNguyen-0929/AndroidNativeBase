package vn.start.data.local.database.converters

import androidx.room.TypeConverter
import vn.start.data.local.database.entities.TaskStatus

class Converters {
    @TypeConverter
    fun fromTaskStatus(value: TaskStatus): String = value.name

    @TypeConverter
    fun toTaskStatus(value: String): TaskStatus = TaskStatus.valueOf(value)
}
