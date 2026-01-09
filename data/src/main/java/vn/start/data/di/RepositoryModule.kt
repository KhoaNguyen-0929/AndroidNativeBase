package vn.start.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.data.repository.DemoRepositoryImpl
import vn.start.data.repository.PomodoroSessionRepositoryImpl
import vn.start.data.repository.ScheduleRepositoryImpl
import vn.start.data.repository.TaskRepositoryImpl
import vn.start.domain.repository.DemoRepository
import vn.start.domain.repository.PomodoroSessionRepository
import vn.start.domain.repository.ScheduleRepository
import vn.start.domain.repository.TaskRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        taskRepositoryImpl: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindPomodoroSessionRepository(
        pomodoroSessionRepositoryImpl: PomodoroSessionRepositoryImpl
    ): PomodoroSessionRepository

    @Binds
    @Singleton
    abstract fun bindScheduleRepository(
        scheduleRepositoryImpl: ScheduleRepositoryImpl
    ): ScheduleRepository

    @Binds
    @Singleton
    abstract fun bindDemoRepository(
        demoRepositoryImpl: DemoRepositoryImpl
    ): DemoRepository
}