package vn.start.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * Task data transfer object for API communication.
 */
data class TaskDto(
    @SerializedName("id")
    val id: Int? = null,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("status")
    val status: String, // "pending", "in_progress", "completed"

    @SerializedName("priority")
    val priority: String? = null, // "low", "medium", "high"

    @SerializedName("created_at")
    val createdAt: Long? = null,

    @SerializedName("updated_at")
    val updatedAt: Long? = null,

    @SerializedName("completed_at")
    val completedAt: Long? = null
)
