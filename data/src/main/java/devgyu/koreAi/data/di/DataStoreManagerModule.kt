package devgyu.koreAi.data.di

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import devgyu.koreAi.data.database.datastore.DataStoreManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataStoreManagerModule {
    private val Context.preferencesDataStore by preferencesDataStore(name = "USER_DATA_STORE")

    @Provides
    @Singleton
    fun providesDataStoreManager(@ApplicationContext context: Context): DataStoreManager =
        DataStoreManager(context.preferencesDataStore)
}