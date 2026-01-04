package devgyu.koreAi.domain.repository

interface FirebaseDatabaseRepository {
    suspend fun fetchFluxUserData(adId: String): Result<Int>
    suspend fun postDecreaseCreatableCnt(): Result<Void>
}