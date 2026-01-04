package devgyu.koreAi.domain.repository

import devgyu.koreAi.domain.entity.FluxImageReq
import devgyu.koreAi.domain.entity.FluxImageRes
import devgyu.koreAi.domain.model.GeneratedImageIdWithPrompt
import devgyu.koreAi.domain.model.FluxImageApiLevel
import kotlinx.coroutines.flow.Flow
import java.io.File

interface FluxRepository {
    suspend fun fetchFluxImage(
        req: FluxImageReq,
        apiLevel: FluxImageApiLevel
    ): Result<FluxImageRes>

    fun getGeneratedImageIdWithPromptList(): Flow<List<GeneratedImageIdWithPrompt>>
    suspend fun deleteGeneratedImageData(generatedImageDataId: Long): Result<Unit>
    fun getCreatableImageCount(): Flow<Int>
    suspend fun downloadFluxImageFile(url: String, contentType: String?): Result<Boolean>
}