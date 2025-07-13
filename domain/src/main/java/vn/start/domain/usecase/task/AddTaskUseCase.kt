package vn.start.domain.usecase.task

import vn.start.domain.model.TaskModel
import vn.start.domain.repository.TaskRepository
import javax.inject.Inject

class AddTaskUseCase @Inject constructor(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: TaskModel) = taskRepository.insertTask(task)
}