package devgyu.koreAi.data.database.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import devgyu.koreAi.data.database.room.entity.GeneratedImageDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GeneratedImageDataDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGeneratedImageData(entity: GeneratedImageDataEntity)

    @Query("SELECT * FROM GeneratedImageDataEntity")
    suspend fun getAll(): List<GeneratedImageDataEntity>

    @Query("SELECT * FROM GeneratedImageDataEntity ORDER BY id DESC")
    fun getGeneratedImageDataList(): Flow<List<GeneratedImageDataEntity>>

    @Query("DELETE FROM GeneratedImageDataEntity WHERE id = :id")
    fun deleteData(id: Long)
}
