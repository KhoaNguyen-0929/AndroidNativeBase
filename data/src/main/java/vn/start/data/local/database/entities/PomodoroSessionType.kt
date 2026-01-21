package vn.start.data.local.database.entities

enum class PomodoroSessionType {
    FOCUS,
    SHORT_BREAK,
    LONG_BREAK;

    companion object {
        fun from(value: String): PomodoroSessionType {
            return valueOf(value)
        }
    }
}

fun PomodoroSessionType.toModelString(): String {
    return this.name
}
