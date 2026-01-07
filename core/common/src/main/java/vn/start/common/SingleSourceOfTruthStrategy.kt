package vn.start.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

/**
 * Single Source of Truth strategy for data fetching.
 * Implements offline-first approach: fetch from cache, then network, update cache.
 *
 * @param E Entity type (DTO/Database entity)
 * @param M Model type (Domain model)
 */
inline fun <E, M> networkBoundResource(
    crossinline fetchFromLocal: () -> Flow<E?>,
    crossinline shouldFetchFromRemote: (E?) -> Boolean = { true },
    crossinline fetchFromRemote: () -> Flow<NetworkResult<E>>,
    crossinline saveToLocal: suspend (E) -> Unit,
    crossinline mapper: (E) -> M
): Flow<NetworkResult<M>> = flow {
    emit(NetworkResult.Loading)

    val localData = fetchFromLocal().first()

    if (localData != null) {
        emit(NetworkResult.Success(mapper(localData)))
    }

    if (shouldFetchFromRemote(localData)) {
        emitAll(
            fetchFromRemote().map { result ->
                when (result) {
                    is NetworkResult.Loading -> NetworkResult.Loading
                    is NetworkResult.Success -> {
                        saveToLocal(result.data)
                        NetworkResult.Success(mapper(result.data))
                    }
                    is NetworkResult.Error -> result
                }
            }
        )
    }
}

/**
 * Simple network-only strategy without caching.
 * Useful for data that should always be fresh.
 */
inline fun <E, M> networkOnlyResource(
    crossinline fetchFromRemote: () -> Flow<NetworkResult<E>>,
    crossinline mapper: (E) -> M
): Flow<NetworkResult<M>> = flow {
    emit(NetworkResult.Loading)
    emitAll(
        fetchFromRemote().map { result ->
            result.map { mapper(it) }
        }
    )
}
