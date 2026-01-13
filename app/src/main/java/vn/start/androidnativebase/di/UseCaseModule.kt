package vn.start.androidnativebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.domain.repository.DemoRepository
import vn.start.domain.usecase.GetDemoCachedUseCase
import vn.start.domain.usecase.GetDemoFreshUseCase
import vn.start.domain.usecase.RefreshDemoUseCase
import javax.inject.Singleton

/**
 * Module for providing use cases at the app level.
 *
 * NOTE: Most use cases are automatically provided by Hilt via their @Inject constructors.
 * This module only needs to provide use cases that have manual constructor requirements.
 */
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // ============ Demo Use Cases ============

    @Singleton
    @Provides
    fun provideGetDemoCachedUseCase(demoRepository: DemoRepository): GetDemoCachedUseCase {
        return GetDemoCachedUseCase(demoRepository)
    }

    @Singleton
    @Provides
    fun provideGetDemoFreshUseCase(demoRepository: DemoRepository): GetDemoFreshUseCase {
        return GetDemoFreshUseCase(demoRepository)
    }

    @Singleton
    @Provides
    fun provideRefreshDemoUseCase(demoRepository: DemoRepository): RefreshDemoUseCase {
        return RefreshDemoUseCase(demoRepository)
    }

    // ============ Task & Schedule Use Cases ============
    // Note: These are automatically provided by Hilt via @Inject constructors
    // No need to manually provide them here
}
