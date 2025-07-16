package vn.start.androidnativebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.domain.repository.DemoRepository
import vn.start.domain.repository.ScheduleRepository
import vn.start.domain.usecase.DemoUseCase
import vn.start.domain.usecase.schedule.AddScheduleUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideDemoUseCase(demoRepository: DemoRepository): DemoUseCase {
        return DemoUseCase(demoRepository)
    }
    @Singleton
    @Provides
    fun provideAddScheduleUseCase(scheduleRepository: ScheduleRepository): AddScheduleUseCase {
        return AddScheduleUseCase(scheduleRepository)
    }

}