package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import devgyu.koreAi.domain.repository.FluxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCreatableImageCountUseCase @Inject constructor(
    private val repository: FluxRepository
) {
    operator fun invoke(): Flow<Int> = repository.getCreatableImageCount()
}