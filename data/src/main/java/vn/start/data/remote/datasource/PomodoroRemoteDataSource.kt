package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.remote.APIServices
import vn.start.data.remote.dto.PomodoroSessionDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Pomodoro session data.
 * Handles all pomodoro session-related API calls.
 */
@Singleton
class PomodoroRemoteDataSource @Inject constructor(
    private val api: APIServices
) {

    /**
     * Get all pomodoro sessions, optionally filtered by task.
     * @param taskId Optional task ID to filter sessions
     */
    fun getPomodoroSessions(taskId: Int? = null): Flow<NetworkResult<List<PomodoroSessionDto>>> {
        return safeApiFlow { api.getPomodoroSessions(taskId) }
    }

    /**
     * Get all pomodoro sessions for a specific task.
     * @param taskId The ID of the task
     */
    fun getPomodoroSessionsForTask(taskId: Int): Flow<NetworkResult<List<PomodoroSessionDto>>> {
        return safeApiFlow { api.getPomodoroSessions(taskId) }
    }

    /**
     * Create a new pomodoro session.
     * @param session The session to create
     */
    fun createPomodoroSession(session: PomodoroSessionDto): Flow<NetworkResult<PomodoroSessionDto>> {
        return safeApiFlow { api.createPomodoroSession(session) }
    }

    /**
     * Update an existing pomodoro session.
     * @param sessionId The ID of the session to update
     * @param session The updated session data
     */
    fun updatePomodoroSession(
        sessionId: Int,
        session: PomodoroSessionDto
    ): Flow<NetworkResult<PomodoroSessionDto>> {
        return safeApiFlow { api.updatePomodoroSession(sessionId, session) }
    }

    /**
     * Complete a pomodoro session.
     * @param sessionId The ID of the session to complete
     * @param session The session with completion timestamp
     */
    fun completePomodoroSession(
        sessionId: Int,
        session: PomodoroSessionDto
    ): Flow<NetworkResult<PomodoroSessionDto>> {
        return safeApiFlow { api.updatePomodoroSession(sessionId, session) }
    }
}
