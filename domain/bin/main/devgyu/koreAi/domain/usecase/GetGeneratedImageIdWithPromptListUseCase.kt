package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.model.GeneratedImageIdWithPrompt
import devgyu.koreAi.domain.repository.FluxRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGeneratedImageIdWithPromptListUseCase @Inject constructor(
    private val repository: FluxRepository
) {
    operator fun invoke(): Flow<List<GeneratedImageIdWithPrompt>> = repository.getGeneratedImageIdWithPromptList()
}