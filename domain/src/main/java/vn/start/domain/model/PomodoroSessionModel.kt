package vn.start.domain.model

data class PomodoroSessionModel(
    val id: Int,
    val taskId: Int,
    val duration: Int,
    val isCompleted: Boolean,
    val createdAt: Long
)
