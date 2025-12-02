package devgyu.koreAi.data.impl

import devgyu.koreAi.data.BuildConfig
import devgyu.koreAi.data.DataStoreKeys
import devgyu.koreAi.data.api.FluxApiService
import devgyu.koreAi.data.database.datastore.DataStoreManager
import devgyu.koreAi.data.database.room.dao.GeneratedImageDataDao
import devgyu.koreAi.data.database.room.entity.GeneratedImageDataEntity
import devgyu.koreAi.data.dto.FluxImageReqDto
import devgyu.koreAi.domain.entity.FluxImageReq
import devgyu.koreAi.domain.entity.FluxImageRes
import devgyu.koreAi.domain.model.GeneratedImageIdWithPrompt
import devgyu.koreAi.domain.model.FluxImageApiLevel
import devgyu.koreAi.domain.repository.FluxRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class FluxImageRepositoryImpl @Inject constructor(
    private val fluxApiService: FluxApiService,
    private val generatedImageDataDao: GeneratedImageDataDao,
    private val dataStoreManager: DataStoreManager
): FluxRepository {
    override suspend fun fetchFluxImage(req: FluxImageReq, apiLevel: FluxImageApiLevel): Result<FluxImageRes> {
        return kotlin.runCatching {
            val fluxEntity = FluxImageReqDto
                .fromDomain(req)
                .copy(numInferenceSteps = apiLevel.numInferenceSteps)

            fluxApiService
                .createFluxImage(fluxEntity, apiLevel.apiUrl)
                .toDomain()
        }.onSuccess { res ->
            // 부적절한 것이 없을때만
            if(res.hasNSFWConcepts.not()) {
                val entity = GeneratedImageDataEntity(
                    prompt = req.prompt,
                    style = req.imageStyleString,
                    imageUrl = res.image.url
                )
                generatedImageDataDao.insertGeneratedImageData(entity)
            }
        }
    }

    override fun getGeneratedImageIdWithPromptList(): Flow<List<GeneratedImageIdWithPrompt>> {
        return generatedImageDataDao.getGeneratedImageDataList()
            .map { it.map { entity -> GeneratedImageIdWithPrompt(entity.id, entity.prompt) } }
    }

    override suspend fun deleteGeneratedImageData(generatedImageDataId: Long): Result<Unit> {
        return kotlin.runCatching { generatedImageDataDao.deleteData(generatedImageDataId) }
    }

    override fun getCreatableImageCount(): Flow<Int> {
        return dataStoreManager
            .getFlow(DataStoreKeys.I_CREATABLE_CNT, 0)
            .filterNotNull()
    }

    override suspend fun downloadFluxImageFile(url: String, contentType: String?): Result<Boolean> {
        return fluxApiService.downloadFluxImage(url, contentType)
    }
}