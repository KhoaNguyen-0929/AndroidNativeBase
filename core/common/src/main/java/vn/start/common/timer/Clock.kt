package vn.start.common.timer

/**
 * Abstraction for time source.
 * Allows for easy testing by injecting a fake clock.
 */
interface Clock {
    /**
     * Returns the current time in milliseconds.
     */
    fun now(): Long
}

/**
 * Default implementation using system time.
 */
class SystemClock : Clock {
    override fun now(): Long = System.currentTimeMillis()
}
