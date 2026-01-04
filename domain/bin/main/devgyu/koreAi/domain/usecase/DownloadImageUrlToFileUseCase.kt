package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.repository.FluxRepository
import java.io.File
import javax.inject.Inject

class DownloadImageUrlToFileUseCase @Inject constructor(
    private val repository: FluxRepository
) {
    suspend operator fun invoke(
        url: String,
        contentType: String?
    ): Result<Boolean> = repository.downloadFluxImageFile(url, contentType)
}