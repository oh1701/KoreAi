package devgyu.koreAi.data.di

import android.content.Context
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.QuotaProjectIdProvider
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.translate.Translate
import com.google.cloud.translate.TranslateOptions
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import timber.log.Timber
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CommonSingletonModule {
    @Provides
    @Singleton
    fun provideTranslate(
        @ApplicationContext context: Context
    ): Translate = TranslateOptions.newBuilder()
        .setCredentials(
            GoogleCredentials.fromStream(
                context.assets.open("koreai-android-09df385c2f1d.json")
            )
        )
        .setTargetLanguage("en")
        .build()
        .service
}