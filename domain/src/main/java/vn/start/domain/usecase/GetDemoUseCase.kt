package vn.start.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

class GetDemoUseCase @Inject constructor(
    private val repository: DemoRepository
) {
    operator fun invoke(): Flow<NetworkResult<DemoModel>> = repository.getDemo()
}
