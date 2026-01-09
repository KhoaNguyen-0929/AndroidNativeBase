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

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
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
}
