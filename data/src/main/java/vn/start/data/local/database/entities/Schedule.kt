package vn.start.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "schedules",
    foreignKeys = [
        ForeignKey(
            entity = Task::class,
            parentColumns = ["id"],
            childColumns = ["taskId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("taskId")]
)
data class Schedule(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val taskId: Int,

    val startTime: Long,

    val endTime: Long
)
