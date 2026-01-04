package devgyu.koreAi.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import devgyu.koreAi.data.database.room.GeneratedImageArchiveDataBase
import devgyu.koreAi.data.database.room.dao.GeneratedImageDataDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDataBaseModule {
    @Provides
    @Singleton
    fun providesCreateFluxImageDataBase(
        @ApplicationContext context: Context
    ): GeneratedImageArchiveDataBase = Room.databaseBuilder(
        context = context,
        klass = GeneratedImageArchiveDataBase::class.java,
        name = "flux_image_database"
    ).fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun providesCreateFluxImageDataDao(
        database: GeneratedImageArchiveDataBase
    ): GeneratedImageDataDao = database.createFluxImageDataDao()
}