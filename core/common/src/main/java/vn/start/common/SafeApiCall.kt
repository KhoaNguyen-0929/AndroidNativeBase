package vn.start.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Response
import vn.start.common.model.NetworkResult
import java.net.SocketTimeoutException
import java.net.UnknownHostException

inline fun <T> safeApiFlow(
    crossinline apiCall: suspend () -> Response<T>
): Flow<NetworkResult<T>> = flow {
    try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(NetworkResult.Success(message = null, data = body))
            } else {
                emit(
                    NetworkResult.Error(
                        message = "Empty response body",
                        code = response.code()
                    )
                )
            }
        } else {
            val errorMsg = parseError(response)
            emit(
                NetworkResult.Error(
                    message = errorMsg,
                    code = response.code()
                )
            )
        }
    } catch (e: Exception) {
        emit(handleException(e))
    }
}.flowOn(Dispatchers.IO)


fun handleException(e: Exception): NetworkResult.Error<Nothing> {
    val isTimeout = e is SocketTimeoutException || e is UnknownHostException
    val msg = when {
        isTimeout -> "Request timed out, please try again."
        e.message.isNullOrBlank().not() -> e.message!!
        else -> "Unexpected error occurred"
    }

    return NetworkResult.Error(
        message = msg,
        code = 1
    )
}


fun parseError(response: Response<*>): String {
    return runCatching {
        ""
//        val errorBody = response.errorBody()?.string().orEmpty()
//        if (errorBody.isBlank()) return "Empty error body"
//
//        val json = JSONObject(errorBody)
//
//        // Ưu tiên field "message", nếu không có thì tìm "error" hoặc fallback
//        json.optString("message")
//            .ifBlank { json.optString("error") }
//            .ifBlank { "Unknown server error" }

    }.getOrElse {
        "Failed to parse error response: {it.localizedMessage ?: "
    }
}

