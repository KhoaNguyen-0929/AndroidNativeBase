package vn.start.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import vn.start.data.remote.dto.DemoDto
import vn.start.data.remote.dto.PomodoroSessionDto
import vn.start.data.remote.dto.ScheduleDto
import vn.start.data.remote.dto.TaskDto

/**
 * Main API service interface.
 * All API endpoints should be defined here with proper documentation.
 *
 * NOTE: Update the base URL in build.gradle.kts flavor configs before using these endpoints.
 */
interface APIServices {

    // ============ Demo Endpoints ============

    /**
     * Get demo data for testing and development.
     * Example: GET /api/v1/demo
     */
    @GET("api/v1/demo")
    suspend fun getDemo(): Response<DemoDto>

    // ============ Task Endpoints ============

    /**
     * Get all tasks for the current user.
     * Supports optional status filtering.
     * Example: GET /api/v1/tasks?status=pending
     */
    @GET("api/v1/tasks")
    suspend fun getTasks(
        @Query("status") status: String? = null
    ): Response<List<TaskDto>>

    /**
     * Get a specific task by ID.
     * Example: GET /api/v1/tasks/{id}
     */
    @GET("api/v1/tasks/{id}")
    suspend fun getTaskById(@Path("id") taskId: Int): Response<TaskDto>

    /**
     * Create a new task.
     * Example: POST /api/v1/tasks
     */
    @POST("api/v1/tasks")
    suspend fun createTask(@Body task: TaskDto): Response<TaskDto>

    /**
     * Update an existing task.
     * Example: PUT /api/v1/tasks/{id}
     */
    @PUT("api/v1/tasks/{id}")
    suspend fun updateTask(
        @Path("id") taskId: Int,
        @Body task: TaskDto
    ): Response<TaskDto>

    /**
     * Delete a task by ID.
     * Example: DELETE /api/v1/tasks/{id}
     */
    @DELETE("api/v1/tasks/{id}")
    suspend fun deleteTask(@Path("id") taskId: Int): Response<Unit>

    // ============ Schedule Endpoints ============

    /**
     * Get all schedules for a specific task.
     * Example: GET /api/v1/tasks/{taskId}/schedules
     */
    @GET("api/v1/tasks/{taskId}/schedules")
    suspend fun getSchedulesForTask(@Path("taskId") taskId: Int): Response<List<ScheduleDto>>

    /**
     * Create a new schedule.
     * Example: POST /api/v1/schedules
     */
    @POST("api/v1/schedules")
    suspend fun createSchedule(@Body schedule: ScheduleDto): Response<ScheduleDto>

    /**
     * Update an existing schedule.
     * Example: PUT /api/v1/schedules/{id}
     */
    @PUT("api/v1/schedules/{id}")
    suspend fun updateSchedule(
        @Path("id") scheduleId: Int,
        @Body schedule: ScheduleDto
    ): Response<ScheduleDto>

    /**
     * Delete a schedule by ID.
     * Example: DELETE /api/v1/schedules/{id}
     */
    @DELETE("api/v1/schedules/{id}")
    suspend fun deleteSchedule(@Path("id") scheduleId: Int): Response<Unit>

    // ============ Pomodoro Session Endpoints ============

    /**
     * Get all pomodoro sessions, optionally filtered by task.
     * Example: GET /api/v1/pomodoro-sessions?taskId={taskId}
     */
    @GET("api/v1/pomodoro-sessions")
    suspend fun getPomodoroSessions(
        @Query("taskId") taskId: Int? = null
    ): Response<List<PomodoroSessionDto>>

    /**
     * Create a new pomodoro session.
     * Example: POST /api/v1/pomodoro-sessions
     */
    @POST("api/v1/pomodoro-sessions")
    suspend fun createPomodoroSession(@Body session: PomodoroSessionDto): Response<PomodoroSessionDto>

    /**
     * Update a pomodoro session (e.g., mark as completed).
     * Example: PUT /api/v1/pomodoro-sessions/{id}
     */
    @PUT("api/v1/pomodoro-sessions/{id}")
    suspend fun updatePomodoroSession(
        @Path("id") sessionId: Int,
        @Body session: PomodoroSessionDto
    ): Response<PomodoroSessionDto>
}