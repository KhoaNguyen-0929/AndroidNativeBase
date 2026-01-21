package vn.start.pomodoro.timer

import javax.inject.Inject

/**
 * Clock abstraction for time-related operations.
 *
 * This interface allows time operations to be testable by providing
 * a way to inject a fixed time in tests.
 */
interface Clock {
    /**
     * Returns the current time in milliseconds since epoch.
     */
    fun now(): Long
}

/**
 * Production implementation of Clock using System.currentTimeMillis().
 */
class SystemClock @Inject constructor() : Clock {
    override fun now(): Long = System.currentTimeMillis()
}

/**
 * Test implementation of Clock that returns a fixed time.
 * Useful for testing timer behavior without real time passing.
 */
class TestClock(private val fixedTime: Long = 0L) : Clock {
    override fun now(): Long = fixedTime
}
