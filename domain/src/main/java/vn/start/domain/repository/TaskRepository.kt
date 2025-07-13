package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.TaskModel

interface TaskRepository {
    fun getAllTasks(): Flow<List<TaskModel>>
    suspend fun getTaskById(id: Int): TaskModel?
    suspend fun insertTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
}
