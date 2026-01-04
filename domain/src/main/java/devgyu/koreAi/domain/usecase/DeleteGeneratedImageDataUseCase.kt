package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.repository.FluxRepository
import javax.inject.Inject

class DeleteGeneratedImageDataUseCase @Inject constructor(
    private val repository: FluxRepository
) {
    suspend operator fun invoke(generatedImageDataId: Long): Result<Unit> =
        repository.deleteGeneratedImageData(generatedImageDataId)
}