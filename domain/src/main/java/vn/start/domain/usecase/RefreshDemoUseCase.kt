package vn.start.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

/**
 * Use case for force refreshing demo data.
 *
 * Use this for:
 * - Pull-to-refresh gestures
 * - Manual refresh buttons
 * - When user wants to force reload data
 */
class RefreshDemoUseCase @Inject constructor(
    private val repository: DemoRepository
) {
    suspend operator fun invoke(): Flow<NetworkResult<DemoModel>> = repository.refreshDemo()
}
