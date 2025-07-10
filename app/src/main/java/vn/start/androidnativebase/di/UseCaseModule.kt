package vn.start.androidnativebase.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.domain.repository.DemoRepository
import vn.start.domain.usecase.DemoUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideDemoUseCase(demoRepository: DemoRepository): DemoUseCase {
        return DemoUseCase(demoRepository)
    }

}