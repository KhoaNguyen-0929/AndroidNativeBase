package vn.start.data.local.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "pomodoro_sessions",
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
data class PomodoroSession(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val taskId: Int,

    val duration: Int,

    val isCompleted: Boolean,

    val createdAt: Long
)
