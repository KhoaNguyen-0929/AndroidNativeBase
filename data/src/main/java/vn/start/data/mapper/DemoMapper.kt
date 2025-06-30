package vn.start.data.mapper

import vn.start.domain.model.DemoModel
import vn.start.model.DemoDto

class DemoMapper : Mapper<DemoDto, DemoModel> {
    override fun fromEntity(entity: DemoDto): DemoModel {
        return DemoModel(
            id = entity.id,
            name = entity.name,
            durationMinutes = entity.durationMinutes
        )
    }
}