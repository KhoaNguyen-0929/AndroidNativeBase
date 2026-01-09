package vn.start.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

/**
 * Use case for fetching fresh demo data from network.
 *
 * Use this when:
 * - You need real-time data
 * - User explicitly requested refresh (pull-to-refresh)
 * - Data changes frequently and shouldn't be cached
 */
class GetDemoFreshUseCase @Inject constructor(
    private val repository: DemoRepository
) {
    operator fun invoke(): Flow<NetworkResult<DemoModel>> = repository.getDemoFresh()
}
