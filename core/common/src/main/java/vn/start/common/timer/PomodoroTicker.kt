package vn.start.common.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * A ticker that emits a tick every [tickIntervalMs] milliseconds.
 *
 * Does NOT know about PomodoroSessionModel - only emits time ticks.
 * CoroutineScope is injected to allow for proper lifecycle management and testing.
 *
 * @param scope The CoroutineScope to use for launching the ticker coroutine
 * @param tickIntervalMs The interval between ticks in milliseconds (default: 1000ms)
 */
class PomodoroTicker(
    private val scope: CoroutineScope,
    private val tickIntervalMs: Long = 1000L
) {
    private var tickerJob: Job? = null

    private val _ticks = MutableSharedFlow<Unit>(replay = 0)

    /**
     * A hot flow that emits a Unit on each tick.
     * Collectors will only receive ticks emitted after they start collecting.
     */
    val ticks: SharedFlow<Unit> = _ticks.asSharedFlow()

    /**
     * Starts the ticker.
     * If already running, this is a no-op.
     */
    fun start() {
        if (isRunning) return

        tickerJob = scope.launch {
            while (isActive) {
                delay(tickIntervalMs)
                _ticks.emit(Unit)
            }
        }
    }

    /**
     * Stops the ticker.
     * If not running, this is a no-op.
     */
    fun stop() {
        tickerJob?.cancel()
        tickerJob = null
    }

    /**
     * Returns true if the ticker is currently running.
     */
    val isRunning: Boolean
        get() = tickerJob?.isActive == true
}
