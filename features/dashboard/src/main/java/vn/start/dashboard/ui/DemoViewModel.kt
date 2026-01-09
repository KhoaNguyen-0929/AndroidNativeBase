package vn.start.dashboard.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import vn.start.common.NetworkResult
import vn.start.common.UiState
import vn.start.common.toUiState
import vn.start.domain.model.DemoModel
import vn.start.domain.usecase.GetDemoCachedUseCase
import vn.start.domain.usecase.GetDemoFreshUseCase
import vn.start.domain.usecase.RefreshDemoUseCase
import javax.inject.Inject

/**
 * Sample ViewModel demonstrating both caching strategies.
 *
 * This shows how to use:
 * 1. getDemoCachedUseCase - Cache-first with background refresh (offline-first)
 * 2. getDemoFreshUseCase - Network-only (always fresh data)
 * 3. refreshDemoUseCase - Force refresh (pull-to-refresh)
 */
@HiltViewModel
class DemoViewModel @Inject constructor(
    private val getDemoCachedUseCase: GetDemoCachedUseCase,
    private val getDemoFreshUseCase: GetDemoFreshUseCase,
    private val refreshDemoUseCase: RefreshDemoUseCase
) : ViewModel() {

    // Cache-first strategy state (recommended for initial load)
    private val _cachedUiState = MutableStateFlow<UiState<DemoModel>>(UiState.Loading)
    val cachedUiState: StateFlow<UiState<DemoModel>> = _cachedUiState.asStateFlow()

    // Network-only strategy state (for fresh data)
    private val _freshUiState = MutableStateFlow<UiState<DemoModel>>(UiState.Idle)
    val freshUiState: StateFlow<UiState<DemoModel>> = _freshUiState.asStateFlow()

    init {
        // Load cached data on initialization (offline-first approach)
        loadCachedDemo()
    }

    /**
     * Load demo data using cache-first strategy.
     *
     * Behavior:
     * - Immediately shows cached data (if available)
     * - Fetches fresh data in background
     * - Updates UI when fresh data arrives
     *
     * Use this for: Initial screen load, offline-first experience
     */
    fun loadCachedDemo() {
        viewModelScope.launch {
            getDemoCachedUseCase().collectLatest { result ->
                _cachedUiState.value = result.toUiState()
            }
        }
    }

    /**
     * Load demo data using network-only strategy.
     *
     * Behavior:
     * - Always fetches from network
     * - Shows loading state
     * - Does not cache the result
     *
     * Use this for: Real-time data, force refresh
     */
    fun loadFreshDemo() {
        viewModelScope.launch {
            getDemoFreshUseCase().collectLatest { result ->
                _freshUiState.value = result.toUiState()
            }
        }
    }

    /**
     * Force refresh demo data.
     *
     * Behavior:
     * - Clears cache
     * - Fetches fresh data from network
     * - Updates cache with new data
     *
     * Use this for: Pull-to-refresh, manual refresh buttons
     */
    fun refreshDemo() {
        viewModelScope.launch {
            refreshDemoUseCase().collectLatest { result ->
                _cachedUiState.value = result.toUiState()
            }
        }
    }
}
