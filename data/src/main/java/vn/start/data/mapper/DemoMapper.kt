package vn.start.data.mapper

import vn.start.common.OneWayMapper
import vn.start.domain.model.DemoModel
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject

class DemoMapper @Inject constructor() : OneWayMapper<DemoDto, DemoModel> {
    override fun fromEntity(entity: DemoDto): DemoModel {
        return DemoModel(
            id = entity.id,
            name = entity.name,
            durationMinutes = entity.durationMinutes
        )
    }
}