package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import javax.inject.Inject

class FetchFluxUserDataUseCase @Inject constructor(
    private val repository: FirebaseDatabaseRepository
) {
    suspend operator fun invoke(adId: String): Result<Int> = repository.fetchFluxUserData(adId)
}