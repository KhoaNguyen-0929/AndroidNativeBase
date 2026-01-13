package vn.start.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Schedule data transfer object for API communication.
 * Represents a scheduled time slot for a task.
 */
data class ScheduleDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("task_id")
    val taskId: Int,

    @SerializedName("start_time")
    val startTime: Long, // Unix timestamp

    @SerializedName("duration")
    val duration: Int, // in seconds

    @SerializedName("completed_at")
    val completedAt: Long? = null,

    @SerializedName("created_at")
    val createdAt: Long? = null
)
