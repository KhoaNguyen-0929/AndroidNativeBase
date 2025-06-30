package vn.start.androidnativebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.data.repository.DemoRepositoryImpl
import vn.start.domain.usecase.DemoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideDemoUseCase(demoRepositoryImpl: DemoRepositoryImpl): DemoUseCase {
        return DemoUseCase(demoRepositoryImpl)
    }
}