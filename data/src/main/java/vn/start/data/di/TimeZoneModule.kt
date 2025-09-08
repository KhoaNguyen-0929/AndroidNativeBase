package vn.start.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import vn.start.common.di.ApplicationScope
import vn.start.data.utils.AndroidTimeZoneMonitor
import vn.start.data.utils.TimeZoneMonitor
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimeZoneModule {
    @Provides
    @Singleton
    fun provideTimeZoneMonitor(
        @ApplicationContext context: Context,
        @ApplicationScope coroutineScope: CoroutineScope,
    ): TimeZoneMonitor = AndroidTimeZoneMonitor(context, coroutineScope)
}
