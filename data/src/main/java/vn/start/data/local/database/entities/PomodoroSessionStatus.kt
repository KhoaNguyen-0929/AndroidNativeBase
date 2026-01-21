package vn.start.data.local.database.entities

enum class PomodoroSessionStatus {
    IDLE,
    RUNNING,
    PAUSED,
    COMPLETED;

    companion object {
        fun from(value: String): PomodoroSessionStatus {
            return valueOf(value)
        }
    }
}

fun PomodoroSessionStatus.toModelString(): String {
    return this.name
}
