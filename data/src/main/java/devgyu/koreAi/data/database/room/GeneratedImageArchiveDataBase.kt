package devgyu.koreAi.data.database.room

import androidx.room.Database
import androidx.room.RoomDatabase
import devgyu.koreAi.data.database.room.dao.GeneratedImageDataDao
import devgyu.koreAi.data.database.room.entity.GeneratedImageDataEntity

@Database(
    entities = [GeneratedImageDataEntity::class],
    version = 4
)
abstract class GeneratedImageArchiveDataBase: RoomDatabase() {
    abstract fun createFluxImageDataDao(): GeneratedImageDataDao
}