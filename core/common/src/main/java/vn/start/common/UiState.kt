package vn.start.common

/**
 * Represents the UI state for a screen or component.
 * Use this in ViewModels to expose state to Compose UI.
 *
 * @param T The type of data in the Success state
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val message: String, val throwable: Throwable? = null) : UiState<Nothing>
}

/**
 * Extension functions for UiState
 */
fun <T> UiState<T>.getOrNull(): T? = (this as? UiState.Success)?.data

fun <T> UiState<T>.errorOrNull(): String? = (this as? UiState.Error)?.message

inline fun <T, R> UiState<T>.map(transform: (T) -> R): UiState<R> = when (this) {
    is UiState.Loading -> UiState.Loading
    is UiState.Success -> UiState.Success(transform(data))
    is UiState.Error -> this
}

inline fun <T> UiState<T>.onSuccess(action: (T) -> Unit): UiState<T> {
    if (this is UiState.Success) action(data)
    return this
}

inline fun <T> UiState<T>.onError(action: (String, Throwable?) -> Unit): UiState<T> {
    if (this is UiState.Error) action(message, throwable)
    return this
}

inline fun <T> UiState<T>.onLoading(action: () -> Unit): UiState<T> {
    if (this is UiState.Loading) action()
    return this
}

/**
 * Convert NetworkResult to UiState
 */
fun <T> NetworkResult<T>.toUiState(): UiState<T> = when (this) {
    is NetworkResult.Loading -> UiState.Loading
    is NetworkResult.Success -> UiState.Success(data)
    is NetworkResult.Error -> UiState.Error(message, exception)
}
