package vn.start.pomodoro.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.pomodoro.timer.Clock
import vn.start.pomodoro.timer.SystemClock
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TimerModule {

    @Provides
    @Singleton
    fun provideClock(): Clock = SystemClock()
}
