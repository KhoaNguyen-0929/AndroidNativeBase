package vn.start.stats.ui

import vn.start.domain.model.DailyPomodoroStats

/**
 * UI state for the Stats screen.
 *
 * Represents all UI-relevant statistics in an immutable data class.
 * No business logic - pure data representation for the UI layer.
 */
data class StatsUiState(
    val isLoading: Boolean = true,
    val todayStats: DailyPomodoroStats? = null,
    val totalFocusSessions: Int = 0,
    val totalFocusTimeSeconds: Int = 0,
    val totalBreakSessions: Int = 0
) {
    /**
     * Formatted total focus time (e.g., "5h 30m").
     */
    val formattedTotalFocusTime: String
        get() {
            val hours = totalFocusTimeSeconds / 3600
            val minutes = (totalFocusTimeSeconds % 3600) / 60
            return when {
                hours > 0 -> "${hours}h ${minutes}m"
                minutes > 0 -> "${minutes}m"
                else -> "0m"
            }
        }

    /**
     * Today's focus time formatted.
     */
    val todayFocusTime: String
        get() = todayStats?.formattedFocusTime ?: "0m"

    /**
     * Today's completed focus sessions.
     */
    val todayFocusSessions: Int
        get() = todayStats?.completedFocusSessions ?: 0

    /**
     * Today's completed sessions total.
     */
    val todayTotalSessions: Int
        get() = todayStats?.totalCompletedSessions ?: 0
}
