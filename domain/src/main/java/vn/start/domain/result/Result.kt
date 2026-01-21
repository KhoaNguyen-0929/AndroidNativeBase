package vn.start.domain.result

/**
 * A generic sealed class representing the result of an operation.
 * Use this for domain layer operations to handle success and error cases.
 *
 * @param T The type of data on success
 */
sealed class Result<out T> {
    
    data class Success<out T>(val data: T) : Result<T>()
    
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : Result<Nothing>()
    
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
    
    /**
     * Returns the data if this is a Success, null otherwise.
     */
    fun getOrNull(): T? = (this as? Success)?.data
    
    /**
     * Returns the error message if this is an Error, null otherwise.
     */
    fun errorOrNull(): String? = (this as? Error)?.message
    
    /**
     * Returns the data if this is a Success, or the result of the default function.
     */
    inline fun getOrElse(default: (Error) -> @UnsafeVariance T): T = when (this) {
        is Success -> data
        is Error -> default(this)
    }
    
    /**
     * Maps the success value using the given transform function.
     */
    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }
    
    /**
     * Maps the success value using a function that returns a Result.
     */
    inline fun <R> flatMap(transform: (T) -> Result<R>): Result<R> = when (this) {
        is Success -> transform(data)
        is Error -> this
    }
    
    /**
     * Executes the given action if this is a Success.
     */
    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }
    
    /**
     * Executes the given action if this is an Error.
     */
    inline fun onError(action: (String, Throwable?) -> Unit): Result<T> {
        if (this is Error) action(message, exception)
        return this
    }
    
    /**
     * Folds the result into a single value.
     */
    inline fun <R> fold(
        onSuccess: (T) -> R,
        onError: (Error) -> R
    ): R = when (this) {
        is Success -> onSuccess(data)
        is Error -> onError(this)
    }
    
    companion object {
        /**
         * Wraps a block of code in a Result, catching any exceptions.
         */
        inline fun <T> runCatching(block: () -> T): Result<T> = try {
            Success(block())
        } catch (e: Exception) {
            Error(e.message ?: "Unknown error", e)
        }
        
        /**
         * Wraps a suspend block of code in a Result, catching any exceptions.
         */
        suspend inline fun <T> runCatchingSuspend(crossinline block: suspend () -> T): Result<T> = try {
            Success(block())
        } catch (e: Exception) {
            Error(e.message ?: "Unknown error", e)
        }
    }
}

/**
 * Extension function to convert Result to UiState.
 * Requires importing vn.start.common.UiState
 */
fun <T> Result<T>.toUiState(): vn.start.common.UiState<T> = when (this) {
    is Result.Success -> vn.start.common.UiState.Success(data)
    is Result.Error -> vn.start.common.UiState.Error(message, exception)
}
