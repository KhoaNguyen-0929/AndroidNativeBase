package vn.start.common.model

sealed class NetworkResult<out T> {
    data class Success<out T>(val message: String?, val data: T?) : NetworkResult<T>()
    data class Error<out T>(val message: String?, val code: Int) : NetworkResult<T>()
}