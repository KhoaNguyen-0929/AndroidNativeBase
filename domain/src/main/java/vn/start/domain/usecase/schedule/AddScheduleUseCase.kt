package vn.start.domain.usecase.schedule

import vn.start.domain.model.ScheduleModel
import vn.start.domain.repository.ScheduleRepository
import javax.inject.Inject

class AddScheduleUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    suspend operator fun invoke(schedule: ScheduleModel) = scheduleRepository.insertSchedule(schedule)
}