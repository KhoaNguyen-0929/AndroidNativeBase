package vn.start.data.mapper

import vn.start.data.local.database.entities.Schedule
import vn.start.domain.model.ScheduleModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ScheduleMapper @Inject constructor() : Mapper<Schedule, ScheduleModel> {

    override fun entityToModel(entity: Schedule): ScheduleModel {
        return ScheduleModel(
            id = entity.id,
            taskId = entity.taskId,
            startTime = entity.startTime,
            endTime = entity.endTime
        )
    }

    override fun modelToEntity(model: ScheduleModel): Schedule {
        return Schedule(
            id = model.id,
            taskId = model.taskId,
            startTime = model.startTime,
            endTime = model.endTime
        )
    }
}
