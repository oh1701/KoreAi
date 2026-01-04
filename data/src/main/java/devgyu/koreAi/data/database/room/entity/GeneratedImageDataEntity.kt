package devgyu.koreAi.data.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GeneratedImageDataEntity(
    val prompt: String,
    val imageUrl: String,
    val style: String,
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)