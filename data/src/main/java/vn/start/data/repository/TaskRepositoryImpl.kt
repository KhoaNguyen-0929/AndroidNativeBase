package vn.start.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.data.local.database.dao.TaskDao
import vn.start.data.mapper.TaskMapper
import vn.start.domain.model.TaskModel
import vn.start.domain.repository.TaskRepository
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao,
    private val taskMapper: TaskMapper
) : TaskRepository {

    override fun getAllTasks(): Flow<List<TaskModel>> {
        return taskDao.getTasks().map { list -> list.map { taskMapper.fromEntity(it) } }
    }

    override suspend fun getTaskById(id: Int): TaskModel? {
        return taskDao.getTaskById(id)?.let { taskMapper.fromEntity(it) }
    }

    override suspend fun insertTask(task: TaskModel) {
        return taskDao.insertTask(taskMapper.toEntity(task))
    }

    override suspend fun updateTask(task: TaskModel) {
        return taskDao.updateTask(taskMapper.toEntity(task))
    }

    override suspend fun deleteTask(task: TaskModel) {
        return taskDao.deleteTask(taskMapper.toEntity(task))
    }
}
