package vn.start.domain.usecase.stats

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.domain.model.DailyPomodoroStats
import vn.start.domain.model.PomodoroSessionModel
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.repository.PomodoroSessionRepository
import java.util.Calendar
import java.util.TimeZone
import javax.inject.Inject

/**
 * Use case for getting daily Pomodoro statistics.
 *
 * Aggregates completed sessions for a specific day and calculates:
 * - Total focus time
 * - Focus session count
 * - Break session counts
 *
 * Only COMPLETED sessions are included in statistics.
 */
class GetDailyStatsUseCase @Inject constructor(
    private val repository: PomodoroSessionRepository
) {
    /**
     * Gets daily stats as a Flow for a specific date.
     *
     * @param dateMillis Timestamp for the day (any time within the day)
     * @return Flow of DailyPomodoroStats
     */
    operator fun invoke(dateMillis: Long): Flow<DailyPomodoroStats> {
        val (startOfDay, endOfDay) = getDayBounds(dateMillis)

        return repository.getAllSessions().map { sessions ->
            val completedSessions = sessions.filter { session ->
                session.status == PomodoroSessionStatus.COMPLETED &&
                session.completedAt != null &&
                session.completedAt in startOfDay until endOfDay
            }

            calculateStats(startOfDay, completedSessions)
        }
    }

    private fun calculateStats(
        date: Long,
        sessions: List<PomodoroSessionModel>
    ): DailyPomodoroStats {
        var totalFocusTimeSeconds = 0
        var focusSessions = 0
        var shortBreaks = 0
        var longBreaks = 0

        sessions.forEach { session ->
            when (session.sessionType) {
                PomodoroSessionType.FOCUS -> {
                    focusSessions++
                    totalFocusTimeSeconds += session.durationSeconds
                }
                PomodoroSessionType.SHORT_BREAK -> shortBreaks++
                PomodoroSessionType.LONG_BREAK -> longBreaks++
            }
        }

        return DailyPomodoroStats(
            date = date,
            totalFocusTimeSeconds = totalFocusTimeSeconds,
            completedFocusSessions = focusSessions,
            completedShortBreaks = shortBreaks,
            completedLongBreaks = longBreaks
        )
    }

    private fun getDayBounds(dateMillis: Long): Pair<Long, Long> {
        val calendar = Calendar.getInstance(TimeZone.getDefault()).apply {
            timeInMillis = dateMillis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        val startOfDay = calendar.timeInMillis

        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val endOfDay = calendar.timeInMillis

        return startOfDay to endOfDay
    }
}
