package vn.start.stats.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import vn.start.domain.usecase.stats.GetDailyStatsUseCase
import vn.start.domain.usecase.stats.GetTotalBreakSessionsUseCase
import vn.start.domain.usecase.stats.GetTotalFocusSessionsUseCase
import vn.start.domain.usecase.stats.GetTotalFocusTimeUseCase
import javax.inject.Inject

/**
 * ViewModel for the Stats screen.
 *
 * Responsibilities:
 * - Collects stats from domain use cases
 * - Exposes UI state as StateFlow
 * - No business logic - delegates to use cases
 */
@HiltViewModel
class StatsViewModel @Inject constructor(
    private val getDailyStatsUseCase: GetDailyStatsUseCase,
    private val getTotalFocusSessionsUseCase: GetTotalFocusSessionsUseCase,
    private val getTotalFocusTimeUseCase: GetTotalFocusTimeUseCase,
    private val getTotalBreakSessionsUseCase: GetTotalBreakSessionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(StatsUiState())
    val uiState: StateFlow<StatsUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        val today = System.currentTimeMillis()

        // Combine all stats flows into a single UI state
        combine(
            getDailyStatsUseCase(today),
            getTotalFocusSessionsUseCase(),
            getTotalFocusTimeUseCase(),
            getTotalBreakSessionsUseCase()
        ) { todayStats, totalFocusSessions, totalFocusTime, totalBreakSessions ->
            StatsUiState(
                isLoading = false,
                todayStats = todayStats,
                totalFocusSessions = totalFocusSessions,
                totalFocusTimeSeconds = totalFocusTime,
                totalBreakSessions = totalBreakSessions
            )
        }
        .onEach { state ->
            _uiState.value = state
        }
        .launchIn(viewModelScope)
    }

    /**
     * Refresh stats (can be called from UI on pull-to-refresh).
     */
    fun refresh() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        loadStats()
    }
}
