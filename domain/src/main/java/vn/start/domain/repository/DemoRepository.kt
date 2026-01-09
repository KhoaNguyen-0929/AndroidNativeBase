package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel

interface DemoRepository {

    /**
     * Cache-first strategy: Returns cached data immediately, then fetches from network.
     * Uses Single Source of Truth pattern with networkBoundResource.
     * Best for: Offline-first apps, data that changes infrequently.
     */
    fun getDemoCached(): Flow<NetworkResult<DemoModel>>

    /**
     * Network-only strategy: Always fetches fresh data from network.
     * Uses networkOnlyResource.
     * Best for: Real-time data, data that changes frequently.
     */
    fun getDemoFresh(): Flow<NetworkResult<DemoModel>>

    /**
     * Force refresh: Clears cache and fetches from network.
     */
    suspend fun refreshDemo(): Flow<NetworkResult<DemoModel>>
}
