package vn.start.domain.usecase.task

import vn.start.domain.model.TaskModel
import vn.start.domain.repository.TaskRepository
import javax.inject.Inject

class GetTaskByIdUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(id: Int): TaskModel? = taskRepository.getTaskById(id)
}