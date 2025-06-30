package vn.start.data.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.common.IoDispatcher
import vn.start.common.model.NetworkResult
import vn.start.data.local.database.datasource.DemoLocalDataSource
import vn.start.data.mapper.Mapper
import vn.start.data.remote.datasource.DemoRemoteDataSource
import vn.start.domain.model.DemoModel
import vn.start.domain.repository.DemoRepository
import vn.start.model.DemoDto
import javax.inject.Inject

class DemoRepositoryImpl @Inject constructor(
    private val remoteDataSource: DemoRemoteDataSource,
    private val mapper: Mapper<DemoDto, DemoModel>
) : DemoRepository {

    override fun demo(): Flow<DemoModel> {
        return remoteDataSource.getDemo().map { result ->
            when (result) {
                is NetworkResult.Success -> {
                    result.data?.let { mapper.fromEntity(it) }
                        ?: throw IllegalStateException("Empty data")
                }

                is NetworkResult.Error -> {
                    throw Exception(result.message ?: "Unknown error (code ${result.code})")
                }
            }
        }
    }
}

