package vn.start.data.mapper

import vn.start.data.local.database.entities.PomodoroSession
import vn.start.domain.model.PomodoroSessionModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PomodoroSessionMapper @Inject constructor() : Mapper<PomodoroSession, PomodoroSessionModel> {

    override fun entityToModel(entity: PomodoroSession): PomodoroSessionModel {
        return PomodoroSessionModel(
            id = entity.id,
            taskId = entity.taskId,
            duration = entity.duration,
            isCompleted = entity.isCompleted,
            createdAt = entity.createdAt
        )
    }

    override fun modelToEntity(model: PomodoroSessionModel): PomodoroSession {
        return PomodoroSession(
            id = model.id,
            taskId = model.taskId,
            duration = model.duration,
            isCompleted = model.isCompleted,
            createdAt = model.createdAt
        )
    }
}
