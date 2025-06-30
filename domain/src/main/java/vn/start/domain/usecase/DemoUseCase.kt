package vn.start.domain.usecase

import kotlinx.coroutines.flow.Flow
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import javax.inject.Inject

class DemoUseCase @Inject constructor(private val repository: DemoRepository) {
    operator fun invoke(): Flow<DemoModel> = repository.demo()
}
