package vn.start.data.local.cache

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Simple in-memory cache for Demo data.
 * This can be replaced with Room database for persistent caching.
 */
@Singleton
class DemoCache @Inject constructor() {

    private val cachedDemo = MutableStateFlow<DemoDto?>(null)


    fun get(): Flow<DemoDto?> = cachedDemo

    /**
     * Save data to cache
     */
    suspend fun save(data: DemoDto) {
        cachedDemo.value = data
    }

    /**
     * Clear cache
     */
    suspend fun clear() {
        cachedDemo.value = null
    }

    /**
     * Check if cache is valid (e.g., not expired)
     */
    fun isValid(): Boolean {
        return cachedDemo.value != null
    }
}
