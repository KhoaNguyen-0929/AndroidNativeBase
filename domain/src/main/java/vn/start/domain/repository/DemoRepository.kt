package vn.start.domain.repository

import kotlinx.coroutines.flow.Flow
import vn.start.common.NetworkResult
import vn.start.domain.model.DemoModel

interface DemoRepository {
    fun getDemo(): Flow<NetworkResult<DemoModel>>
}