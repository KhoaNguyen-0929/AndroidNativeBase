package vn.start.domain.model

data class ScheduleModel(
    val id: Int,
    val taskId: Int,
    val startTime: Long,
    val endTime: Long
)
