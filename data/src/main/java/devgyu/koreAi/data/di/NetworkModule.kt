package devgyu.koreAi.data.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import devgyu.koreAi.data.ktor.KtorClient
import io.ktor.client.HttpClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideAuthenticatedHttpClient(
        @ApplicationContext context: Context
    ): HttpClient = KtorClient(context).ktorClient
}