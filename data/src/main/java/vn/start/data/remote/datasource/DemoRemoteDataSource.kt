package vn.start.data.remote.datasource

import kotlinx.coroutines.flow.Flow
import vn.start.common.model.NetworkResult
import vn.start.common.safeApiFlow
import vn.start.data.remote.APIServices
import vn.start.model.DemoDto

class DemoRemoteDataSource(private val api: APIServices) {
    fun getDemo(): Flow<NetworkResult<DemoDto>> = safeApiFlow {
        api.getDemo()
    }
}