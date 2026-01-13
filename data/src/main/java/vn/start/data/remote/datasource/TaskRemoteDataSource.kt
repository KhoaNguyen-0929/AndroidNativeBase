package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.remote.APIServices
import vn.start.data.remote.dto.TaskDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Task data.
 * Handles all task-related API calls.
 */
@Singleton
class TaskRemoteDataSource @Inject constructor(
    private val api: APIServices
) {

    /**
     * Get all tasks for the current user.
     * @param status Optional status filter (pending, in_progress, completed)
     */
    fun getTasks(status: String? = null): Flow<NetworkResult<List<TaskDto>>> {
        return safeApiFlow { api.getTasks(status) }
    }

    /**
     * Get a specific task by ID.
     * @param taskId The ID of the task to retrieve
     */
    fun getTaskById(taskId: Int): Flow<NetworkResult<TaskDto>> {
        return safeApiFlow { api.getTaskById(taskId) }
    }

    /**
     * Create a new task.
     * @param task The task to create
     */
    fun createTask(task: TaskDto): Flow<NetworkResult<TaskDto>> {
        return safeApiFlow { api.createTask(task) }
    }

    /**
     * Update an existing task.
     * @param taskId The ID of the task to update
     * @param task The updated task data
     */
    fun updateTask(taskId: Int, task: TaskDto): Flow<NetworkResult<TaskDto>> {
        return safeApiFlow { api.updateTask(taskId, task) }
    }

    /**
     * Delete a task by ID.
     * @param taskId The ID of the task to delete
     */
    fun deleteTask(taskId: Int): Flow<NetworkResult<Unit>> {
        return safeApiFlow { api.deleteTask(taskId) }
    }
}
