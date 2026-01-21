package vn.start.domain.usecase.stats

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vn.start.domain.model.PomodoroSessionStatus
import vn.start.domain.model.PomodoroSessionType
import vn.start.domain.repository.PomodoroSessionRepository
import javax.inject.Inject

/**
 * Use case for getting the total count of completed focus sessions.
 */
class GetTotalFocusSessionsUseCase @Inject constructor(
    private val repository: PomodoroSessionRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllSessions().map { sessions ->
            sessions.count { session ->
                session.status == PomodoroSessionStatus.COMPLETED &&
                session.sessionType == PomodoroSessionType.FOCUS
            }
        }
    }
}

/**
 * Use case for getting the total focus time in seconds across all sessions.
 */
class GetTotalFocusTimeUseCase @Inject constructor(
    private val repository: PomodoroSessionRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllSessions().map { sessions ->
            sessions
                .filter { session ->
                    session.status == PomodoroSessionStatus.COMPLETED &&
                    session.sessionType == PomodoroSessionType.FOCUS
                }
                .sumOf { it.durationSeconds }
        }
    }
}

/**
 * Use case for getting the total count of completed break sessions.
 */
class GetTotalBreakSessionsUseCase @Inject constructor(
    private val repository: PomodoroSessionRepository
) {
    operator fun invoke(): Flow<Int> {
        return repository.getAllSessions().map { sessions ->
            sessions.count { session ->
                session.status == PomodoroSessionStatus.COMPLETED &&
                (session.sessionType == PomodoroSessionType.SHORT_BREAK ||
                 session.sessionType == PomodoroSessionType.LONG_BREAK)
            }
        }
    }
}
