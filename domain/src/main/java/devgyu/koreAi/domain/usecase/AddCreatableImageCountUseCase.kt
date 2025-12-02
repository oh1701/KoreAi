package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import javax.inject.Inject

class AddCreatableImageCountUseCase @Inject constructor(
    private val repository: FirebaseDatabaseRepository
) {
    suspend operator fun invoke(): Result<Void> {
        return repository.postIncreaseCreatableCnt()
    }
}