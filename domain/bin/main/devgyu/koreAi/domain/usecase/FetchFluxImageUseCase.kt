package devgyu.koreAi.domain.usecase

import devgyu.koreAi.domain.entity.FluxImageReq
import devgyu.koreAi.domain.entity.FluxImageRes
import devgyu.koreAi.domain.model.FluxImageApiLevel
import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import devgyu.koreAi.domain.repository.FluxRepository
import javax.inject.Inject

/**
 * API 호출
 * 성공 시 Count 깎음
 * */
class FetchFluxImageUseCase @Inject constructor(
    private val repository: FluxRepository,
    private val firebaseDatabaseRepository: FirebaseDatabaseRepository
) {
    suspend operator fun invoke(
        req: FluxImageReq,
        apiLevel: FluxImageApiLevel = FluxImageApiLevel.Schnell
    ): Result<FluxImageRes> = repository.fetchFluxImage(req, apiLevel).onSuccess {
        firebaseDatabaseRepository.postDecreaseCreatableCnt()
    }
}