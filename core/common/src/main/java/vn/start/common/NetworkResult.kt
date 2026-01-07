package vn.start.common

/**
 * Represents the result of a network operation.
 * - [Loading] - Operation is in progress
 * - [Success] - Operation completed successfully with data
 * - [Error] - Operation failed with an error message and optional code
 */
sealed class NetworkResult<out T> {

    data object Loading : NetworkResult<Nothing>()

    data class Success<out T>(val data: T) : NetworkResult<T>()

    data class Error(
        val message: String,
        val code: Int? = null,
        val exception: Throwable? = null
    ) : NetworkResult<Nothing>()

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error

    fun getOrNull(): T? = (this as? Success)?.data

    fun errorOrNull(): String? = (this as? Error)?.message

    inline fun <R> map(transform: (T) -> R): NetworkResult<R> = when (this) {
        is Loading -> Loading
        is Success -> Success(transform(data))
        is Error -> this
    }

    inline fun onSuccess(action: (T) -> Unit): NetworkResult<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (String, Int?, Throwable?) -> Unit): NetworkResult<T> {
        if (this is Error) action(message, code, exception)
        return this
    }

    inline fun onLoading(action: () -> Unit): NetworkResult<T> {
        if (this is Loading) action()
        return this
    }
}
