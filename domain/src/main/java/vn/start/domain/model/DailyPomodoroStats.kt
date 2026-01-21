package vn.start.domain.model

/**
 * Represents daily productivity statistics for Pomodoro sessions.
 *
 * @param date The date these stats are for (start of day timestamp)
 * @param totalFocusTimeSeconds Total focus time in seconds for completed focus sessions
 * @param completedFocusSessions Number of completed focus sessions
 * @param completedShortBreaks Number of completed short break sessions
 * @param completedLongBreaks Number of completed long break sessions
 */
data class DailyPomodoroStats(
    val date: Long,
    val totalFocusTimeSeconds: Int = 0,
    val completedFocusSessions: Int = 0,
    val completedShortBreaks: Int = 0,
    val completedLongBreaks: Int = 0
) {
    /**
     * Total focus time formatted as hours and minutes.
     */
    val formattedFocusTime: String
        get() {
            val hours = totalFocusTimeSeconds / 3600
            val minutes = (totalFocusTimeSeconds % 3600) / 60
            return when {
                hours > 0 -> "${hours}h ${minutes}m"
                else -> "${minutes}m"
            }
        }

    /**
     * Total number of completed sessions (focus + breaks).
     */
    val totalCompletedSessions: Int
        get() = completedFocusSessions + completedShortBreaks + completedLongBreaks
}
