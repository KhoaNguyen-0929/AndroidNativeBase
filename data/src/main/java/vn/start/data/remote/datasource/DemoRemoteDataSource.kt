package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.BuildConfig
import vn.start.data.remote.APIServices
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Remote data source for Demo data.
 *
 * Uses mock data in debug builds for development and testing.
 * Uses real API calls in release builds.
 */
@Singleton
class DemoRemoteDataSource @Inject constructor(
    private val api: APIServices,
    private val mockDataSource: DemoMockDataSource
) {
    /**
     * Get demo data from API or mock source based on build type.
     * Debug builds use mock data, release builds use real API.
     */
    fun getDemo(): Flow<NetworkResult<DemoDto>> {
        return if (BuildConfig.DEBUG) {
            mockDataSource.getMockDemo()
        } else {
            safeApiFlow { api.getDemo() }
        }
    }

    /**
     * Get demo data with error for testing error states.
     * Only works in debug mode with mock data.
     */
    fun getDemoError(): Flow<NetworkResult<DemoDto>> {
        return if (BuildConfig.DEBUG) {
            mockDataSource.getMockDemoError()
        } else {
            // In release, return actual API call
            safeApiFlow { api.getDemo() }
        }
    }

    /**
     * Force refresh demo data from API (ignores mock).
     */
    fun getDemoFresh(): Flow<NetworkResult<DemoDto>> {
        return safeApiFlow { api.getDemo() }
    }
}
