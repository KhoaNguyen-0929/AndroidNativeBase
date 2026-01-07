package vn.start.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.common.NetworkResult
import vn.start.common.OneWayMapper
import vn.start.data.remote.datasource.DemoRemoteDataSource
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import vn.start.data.remote.dto.DemoDto
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val remoteDataSource: DemoRemoteDataSource,
    private val mapper: OneWayMapper<DemoDto, DemoModel>
) : DemoRepository {

    override fun getDemo(): Flow<NetworkResult<DemoModel>> {
        return remoteDataSource.getDemo().map { result ->
            result.map { mapper.fromEntity(it) }
        }
    }
}
