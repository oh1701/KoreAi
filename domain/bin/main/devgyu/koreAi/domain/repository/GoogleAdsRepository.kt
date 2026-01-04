package devgyu.koreAi.domain.repository

interface GoogleAdsRepository {
    suspend fun call(): Result<String>
}