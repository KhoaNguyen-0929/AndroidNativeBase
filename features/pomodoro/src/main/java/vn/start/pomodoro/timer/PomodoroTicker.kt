package vn.start.pomodoro.timer

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.RENDEZVOUS
import javax.inject.Inject

/**
 * A ticker that emits ticks every second.
 *
 * Uses coroutines to produce a flow of ticks that can be started and stopped.
 * Ticks are produced via a Channel to allow backpressure handling.
 */
class PomodoroTicker @Inject constructor() {

    private val tickerChannel: Channel<Unit> = Channel(RENDEZVOUS)

    /**
     * Starts the ticker, emitting a tick every second.
     *
     * @return A Channel that emits Unit every second
     */
    fun start(): Channel<Unit> {
        return tickerChannel
    }

    /**
     * Stops the ticker by closing the channel.
     */
    fun stop() {
        tickerChannel.close()
    }
}
