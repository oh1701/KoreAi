package devgyu.koreAi.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import devgyu.koreAi.data.impl.FirebaseDatabaseRepositoryImpl
import devgyu.koreAi.data.impl.FluxImageRepositoryImpl
import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import devgyu.koreAi.domain.repository.FluxRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindFluxRepository(
        fluxImageRepositoryImpl: FluxImageRepositoryImpl
    ): FluxRepository

    @Binds
    abstract fun bindFirebaseDatabaseRepository(
        firebaseDatabaseRepositoryImpl: FirebaseDatabaseRepositoryImpl
    ): FirebaseDatabaseRepository
}