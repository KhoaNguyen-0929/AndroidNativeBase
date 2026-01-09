package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.remote.APIServices
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Demo data.
 *
 * Set USE_MOCK_DATA = true to use mock data for demo/testing.
 * Set USE_MOCK_DATA = false to use real API calls.
 */
@Singleton
class DemoRemoteDataSource @Inject constructor(
    private val api: APIServices,
    private val mockDataSource: DemoMockDataSource
) {
    companion object {
        const val USE_MOCK_DATA = true // Set to false for real API
    }

    /**
     * Get demo data from API or mock source based on USE_MOCK_DATA flag.
     */
    fun getDemo(): Flow<NetworkResult<DemoDto>> {
        return if (USE_MOCK_DATA) {
            mockDataSource.getMockDemo()
        } else {
            // API call commented out - URL not known
            // safeApiFlow { api.getDemo() }
            mockDataSource.getMockDemo() // Use mock data instead
        }
    }

    /**
     * Get demo data with error for testing error states.
     * Only works in mock mode.
     */
    fun getDemoError(): Flow<NetworkResult<DemoDto>> {
        return if (USE_MOCK_DATA) {
            mockDataSource.getMockDemoError()
        } else {
            // API call commented out - URL not known
            // safeApiFlow { api.getDemo() }
            mockDataSource.getMockDemoError() // Use mock data instead
        }
    }
}
