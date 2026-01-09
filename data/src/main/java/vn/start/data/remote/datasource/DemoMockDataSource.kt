package vn.start.data.remote.datasource

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import vn.start.common.NetworkResult
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Mock data source for demo purposes.
 * Returns dummy data after a simulated network delay.
 */
@Singleton
class DemoMockDataSource @Inject constructor() {

    /**
     * Get mock demo data with simulated network delay.
     * Use this for demo/testing without real API.
     */
    fun getMockDemo(): Flow<NetworkResult<DemoDto>> = flow {
        emit(NetworkResult.Loading)

        // Simulate network delay
        delay(1000)

        val mockData = DemoDto(
            id = "demo-001",
            name = "Morning Yoga Session",
            durationMinutes = 30
        )

        emit(NetworkResult.Success(mockData))
    }

    /**
     * Get mock demo data with error for testing error handling.
     */
    fun getMockDemoError(): Flow<NetworkResult<DemoDto>> = flow {
        emit(NetworkResult.Loading)

        // Simulate network delay
        delay(800)

        emit(NetworkResult.Error("Network error: Unable to fetch data"))
    }

    companion object {
        /**
         * Collection of mock demo data for various scenarios.
         */
        val MOCK_DEMOS = listOf(
            DemoDto("demo-001", "Morning Yoga Session", 30),
            DemoDto("demo-002", "HIIT Workout", 45),
            DemoDto("demo-003", "Evening Meditation", 15),
            DemoDto("demo-004", "Strength Training", 60),
            DemoDto("demo-005", "Cardio Blast", 40),
            DemoDto("demo-006", "Pilates Core", 35),
            DemoDto("demo-007", "Stretching Routine", 20),
            DemoDto("demo-008", "Dance Fitness", 50)
        )
    }
}
