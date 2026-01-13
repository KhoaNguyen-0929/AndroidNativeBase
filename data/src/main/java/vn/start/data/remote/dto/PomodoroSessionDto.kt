package vn.start.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Pomodoro session data transfer object for API communication.
 * Represents a focused work session using the Pomodoro technique.
 */
data class PomodoroSessionDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("task_id")
    val taskId: Int,

    @SerializedName("duration")
    val duration: Int, // in seconds (typically 1500 = 25 minutes)

    @SerializedName("completed_at")
    val completedAt: Long? = null,

    @SerializedName("created_at")
    val createdAt: Long
)
