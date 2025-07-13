package vn.start.domain.model

data class TaskModel(
    val id: Int = 0,
    val title: String,
    val description: String,
    val priority: Int,
    val status: TaskStatusModel,
    val createdAt: Long,
    val updatedAt: Long
)

enum class TaskStatusModel {
    PENDING,
    IN_PROGRESS,
    DONE
}
