package vn.start.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import vn.start.data.repository.DemoRepositoryImpl
import vn.start.domain.repository.DemoRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    /**
     * @DemoRepositoryImpl already has an @Inject constructor, which means Hilt knows how to instantiate it.
     * You just need to tell Hilt: when you need
     * @return DemoRepository → provide @DemoRepositoryImpl.
     * This is exactly the purpose of @Binds: map interface ↔ implementation.
     */
    @Binds
    abstract fun bindUserRepository(
        demoRepositoryImpl: DemoRepositoryImpl
    ): DemoRepository
}