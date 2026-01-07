package vn.start.common

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Wraps a Retrofit API call in a Flow that emits NetworkResult states.
 * Handles success, error responses, and exceptions.
 */
inline fun <T> safeApiFlow(
    crossinline apiCall: suspend () -> Response<T>
): Flow<NetworkResult<T>> = flow {
    emit(NetworkResult.Loading)
    try {
        val response = apiCall()

        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                emit(NetworkResult.Success(data = body))
            } else {
                emit(
                    NetworkResult.Error(
                        message = "Empty response body",
                        code = response.code()
                    )
                )
            }
        } else {
            val errorMsg = parseErrorBody(response)
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

/**
 * Handles exceptions and converts them to NetworkResult.Error
 */
fun handleException(e: Exception): NetworkResult.Error {
    val message = when (e) {
        is SocketTimeoutException -> "Request timed out. Please try again."
        is UnknownHostException -> "No internet connection. Please check your network."
        else -> e.message?.takeIf { it.isNotBlank() } ?: "An unexpected error occurred"
    }
    return NetworkResult.Error(
        message = message,
        exception = e
    )
}

/**
 * Parses error body from Retrofit response.
 * Attempts to extract "message" or "error" field from JSON.
 */
fun parseErrorBody(response: Response<*>): String {
    return runCatching {
        val errorBody = response.errorBody()?.string().orEmpty()
        if (errorBody.isBlank()) return "Empty error body"

        val jsonObject = Json.parseToJsonElement(errorBody).jsonObject
        val message = jsonObject["message"]?.jsonPrimitive?.contentOrNull
        val error = jsonObject["error"]?.jsonPrimitive?.contentOrNull

        when {
            !message.isNullOrBlank() -> message
            !error.isNullOrBlank() -> error
            else -> "Unknown server error"
        }
    }.getOrElse {
        "Failed to parse error response: ${it.localizedMessage}"
    }
}
