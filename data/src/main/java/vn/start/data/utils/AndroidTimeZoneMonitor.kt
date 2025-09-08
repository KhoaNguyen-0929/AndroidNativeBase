package vn.start.data.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.job
import kotlinx.datetime.TimeZone
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidTimeZoneMonitor @Inject constructor(
    @ApplicationContext private val context: Context,
    coroutineScope: CoroutineScope,
) : TimeZoneMonitor {

    private val _timeZone = MutableStateFlow(TimeZone.currentSystemDefault())
    override val currentTimeZone: StateFlow<TimeZone> = _timeZone.asStateFlow()

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == Intent.ACTION_TIMEZONE_CHANGED) {
                _timeZone.value = TimeZone.currentSystemDefault()
            }
        }
    }

    init {
        context.registerReceiver(receiver, IntentFilter(Intent.ACTION_TIMEZONE_CHANGED))
        // Make sure to unregister when scope is cancelled
        coroutineScope.coroutineContext.job.invokeOnCompletion {
            context.unregisterReceiver(receiver)
        }
    }
}
