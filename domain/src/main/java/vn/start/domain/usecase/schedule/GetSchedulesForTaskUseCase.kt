package vn.start.domain.usecase.schedule

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.ScheduleModel
import vn.start.domain.repository.ScheduleRepository
import javax.inject.Inject

class GetSchedulesForTaskUseCase @Inject constructor(
    private val scheduleRepository: ScheduleRepository
) {
    operator fun invoke(taskId: Int): Flow<List<ScheduleModel>> = 
        scheduleRepository.getSchedulesForTask(taskId)
}