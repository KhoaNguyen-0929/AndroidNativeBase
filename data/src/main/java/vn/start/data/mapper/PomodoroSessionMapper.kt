package vn.start.data.mapper

import vn.start.common.Mapper
import vn.start.data.local.database.entities.PomodoroSession
import vn.start.data.local.database.entities.PomodoroSessionStatus
import vn.start.data.local.database.entities.PomodoroSessionType
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus as DomainStatus
import vn.start.domain.model.PomodoroSessionType as DomainType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PomodoroSessionMapper @Inject constructor() : Mapper<PomodoroSession, PomodoroSessionModel> {

    override fun fromEntity(entity: PomodoroSession): PomodoroSessionModel {
        return PomodoroSessionModel(
            id = entity.id,
            sessionType = mapSessionType(entity.sessionType),
            durationSeconds = entity.durationSeconds,
            status = mapSessionStatus(entity.status),
            taskId = entity.taskId,
            remainingSeconds = entity.remainingSeconds,
            createdAt = entity.createdAt,
            startedAt = entity.startedAt,
            completedAt = entity.completedAt
        )
    }

    override fun toEntity(model: PomodoroSessionModel): PomodoroSession {
        return PomodoroSession(
            id = model.id,
            taskId = model.taskId,
            sessionType = model.sessionType.name,
            durationSeconds = model.durationSeconds,
            status = model.status.name,
            remainingSeconds = model.remainingSeconds,
            createdAt = model.createdAt,
            startedAt = model.startedAt,
            completedAt = model.completedAt
        )
    }

    private fun mapSessionType(type: String): DomainType {
        return DomainType.valueOf(type)
    }

    private fun mapSessionStatus(status: String): DomainStatus {
        return DomainStatus.valueOf(status)
    }

    private fun DomainType.toEntityString(): String {
        return this.name
    }

    private fun DomainStatus.toEntityString(): String {
        return this.name
    }
}
