package vn.start.domain.model

data class PomodoroSessionModel(
    val id: Int = 0,
    val sessionType: PomodoroSessionType,
    val durationSeconds: Int,
    val status: PomodoroSessionStatus = PomodoroSessionStatus.IDLE,
    val taskId: Int? = null,
    val remainingSeconds: Int = durationSeconds,
    val createdAt: Long = System.currentTimeMillis(),
    val startedAt: Long? = null,
    val completedAt: Long? = null
) {
    init {
        require(durationSeconds > 0) { "Duration must be positive" }
        require(remainingSeconds in 0..durationSeconds) {
            "Remaining seconds must be between 0 and durationSeconds"
        }
    }

    fun start(): PomodoroSessionModel {
        require(status == PomodoroSessionStatus.IDLE) {
            "Can only start from IDLE status"
        }
        return copy(
            status = PomodoroSessionStatus.RUNNING,
            startedAt = System.currentTimeMillis()
        )
    }

    fun pause(): PomodoroSessionModel {
        require(status == PomodoroSessionStatus.RUNNING) {
            "Can only pause from RUNNING status"
        }
        return copy(status = PomodoroSessionStatus.PAUSED)
    }

    fun resume(): PomodoroSessionModel {
        require(status == PomodoroSessionStatus.PAUSED) {
            "Can only resume from PAUSED status"
        }
        return copy(status = PomodoroSessionStatus.RUNNING)
    }

    fun complete(): PomodoroSessionModel {
        require(status == PomodoroSessionStatus.RUNNING || status == PomodoroSessionStatus.PAUSED) {
            "Can only complete from RUNNING or PAUSED status"
        }
        return copy(
            status = PomodoroSessionStatus.COMPLETED,
            remainingSeconds = 0,
            completedAt = System.currentTimeMillis()
        )
    }

    fun updateRemaining(seconds: Int): PomodoroSessionModel {
        require(seconds in 0..durationSeconds) {
            "Remaining seconds must be between 0 and durationSeconds"
        }
        return copy(remainingSeconds = seconds)
    }

    val progress: Float
        get() = if (durationSeconds > 0) {
            (durationSeconds - remainingSeconds).toFloat() / durationSeconds
        } else {
            0f
        }

    val isIdle: Boolean
        get() = status == PomodoroSessionStatus.IDLE

    val isRunning: Boolean
        get() = status == PomodoroSessionStatus.RUNNING

    val isPaused: Boolean
        get() = status == PomodoroSessionStatus.PAUSED

    val isCompleted: Boolean
        get() = status == PomodoroSessionStatus.COMPLETED
}
