package vn.start.data.repository

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.common.OneWayMapper
import vn.start.common.networkBoundResource
import vn.start.common.networkOnlyResource
import vn.start.data.local.cache.DemoCache
import vn.start.data.remote.datasource.DemoRemoteDataSource
import vn.start.data.remote.dto.DemoDto
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val remoteDataSource: DemoRemoteDataSource,
    private val cache: DemoCache,
    private val mapper: OneWayMapper<DemoDto, DemoModel>
) : DemoRepository {

    /**
     * Cache-first strategy using networkBoundResource.
     *
     * Flow:
     * 1. Emit Loading
     * 2. Fetch from cache and emit immediately (if available)
     * 3. Fetch from network
     * 4. Save network response to cache
     * 5. Emit final result
     *
     * This provides offline-first experience - users see cached data instantly
     * while fresh data is being fetched.
     */
    override fun getDemoCached(): Flow<NetworkResult<DemoModel>> {
        return networkBoundResource(
            fetchFromLocal = { cache.get() },
            shouldFetchFromRemote = { cachedData ->
                // Fetch from network if cache is empty
                cachedData == null
            },
            fetchFromRemote = { remoteDataSource.getDemo() },
            saveToLocal = { demoDto -> cache.save(demoDto) },
            mapper = { demoDto -> mapper.fromEntity(demoDto) }
        )
    }

    /**
     * Network-only strategy using networkOnlyResource.
     *
     * Flow:
     * 1. Emit Loading
     * 2. Fetch from network
     * 3. Emit result (without caching)
     *
     * Use this for real-time data that should always be fresh,
     * or for data that shouldn't be cached.
     */
    override fun getDemoFresh(): Flow<NetworkResult<DemoModel>> {
        return networkOnlyResource(
            fetchFromRemote = { remoteDataSource.getDemo() },
            mapper = { demoDto -> mapper.fromEntity(demoDto) }
        )
    }

    /**
     * Force refresh: Clear cache and fetch fresh data.
     * Use this for pull-to-refresh functionality.
     */
    override suspend fun refreshDemo(): Flow<NetworkResult<DemoModel>> {
        cache.clear()
        return networkOnlyResource(
            fetchFromRemote = { remoteDataSource.getDemo() },
            mapper = { demoDto -> mapper.fromEntity(demoDto) }
        )
    }
}
