package vn.start.domain.mapper

class DemoMapper : Mapper<WorkoutDto, Workout> {
    override fun fromEntity(entity: WorkoutDto): Workout {
        return Workout(
            id = entity.id,
            name = entity.name,
            durationMinutes = entity.durationMinutes
        )
    }
}