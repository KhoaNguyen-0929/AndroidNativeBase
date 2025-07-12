package vn.start.data.local.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,
    val description: String,
    val priority: Int,

    @ColumnInfo(name = "status")
    val status: TaskStatus = TaskStatus.PENDING,

    val createdAt: Long,
    val updatedAt: Long
)