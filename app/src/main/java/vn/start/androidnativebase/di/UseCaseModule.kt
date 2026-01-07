package vn.start.androidnativebase.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.domain.repository.DemoRepository
import vn.start.domain.usecase.GetDemoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetDemoUseCase(demoRepository: DemoRepository): GetDemoUseCase {
        return GetDemoUseCase(demoRepository)
    }
}
