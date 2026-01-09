package vn.start.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

/**
 * Use case for fetching demo data with caching.
 *
 * This is the recommended approach for most scenarios:
 * - Instant UI response from cache
 * - Background refresh from network
 * - Works offline after first fetch
 */
class GetDemoCachedUseCase @Inject constructor(
    private val repository: DemoRepository
) {
    operator fun invoke(): Flow<NetworkResult<DemoModel>> = repository.getDemoCached()
}
