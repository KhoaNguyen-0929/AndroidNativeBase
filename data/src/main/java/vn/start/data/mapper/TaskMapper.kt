package vn.start.data.mapper

import vn.start.data.local.database.entities.Task
import vn.start.data.local.database.entities.TaskStatus
import vn.start.domain.model.TaskModel
import vn.start.domain.model.TaskStatusModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskMapper @Inject constructor() : Mapper<Task, TaskModel> {

    override fun entityToModel(entity: Task): TaskModel {
        return TaskModel(
            id = entity.id,
            title = entity.title,
            description = entity.description,
            priority = entity.priority,
            status = mapStatusToModel(entity.status),
            createdAt = entity.createdAt,
            updatedAt = entity.updatedAt
        )
    }

    override fun modelToEntity(model: TaskModel): Task {
        return Task(
            id = model.id,
            title = model.title,
            description = model.description,
            priority = model.priority,
            status = mapStatusToEntity(model.status),
            createdAt = model.createdAt,
            updatedAt = model.updatedAt
        )
    }

    // Internal mapper functions
    private fun mapStatusToModel(from: TaskStatus): TaskStatusModel {
        return when (from) {
            TaskStatus.PENDING -> TaskStatusModel.PENDING
            TaskStatus.IN_PROGRESS -> TaskStatusModel.IN_PROGRESS
            TaskStatus.DONE -> TaskStatusModel.DONE
        }
    }

    private fun mapStatusToEntity(from: TaskStatusModel): TaskStatus {
        return when (from) {
            TaskStatusModel.PENDING -> TaskStatus.PENDING
            TaskStatusModel.IN_PROGRESS -> TaskStatus.IN_PROGRESS
            TaskStatusModel.DONE -> TaskStatus.DONE
        }
    }
}
