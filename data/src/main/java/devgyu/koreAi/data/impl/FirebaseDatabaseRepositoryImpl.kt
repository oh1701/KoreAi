package devgyu.koreAi.data.impl

import com.google.firebase.Firebase
import com.google.firebase.database.database
import devgyu.koreAi.data.DataStoreKeys
import devgyu.koreAi.data.database.datastore.DataStoreManager
import devgyu.koreAi.data.getUserReference
import devgyu.koreAi.domain.AdIDNotFoundException
import devgyu.koreAi.domain.repository.FirebaseDatabaseRepository
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class FirebaseDatabaseRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
): FirebaseDatabaseRepository {
    private val firebaseDatabase = Firebase.database.reference

    /**
     * 0. 이전에 Firebase 데이터를 가져왔는지 확인
     * 1. 현재 AdId 와 비교
     * 2. AdId 가 DB 에 존재하는 경우 Object 가져옴
     * 3. 존재하지 않으면 현재 AdId 설정 후 기본 Object 가져옴
     */
    override suspend fun fetchFluxUserData(adId: String): Result<Int> {
        val isExistUserAdId = firebaseDatabase
            .child("user_adId")
            .child(adId)
            .get()
            .await()
            .exists()

        return when (isExistUserAdId) {
            true -> fetchCreatableCnt(adId)
            else -> postUserAdId(adId)
        }.onSuccess { creatableCnt ->
            dataStoreManager.edit(DataStoreKeys.B_FETCH_FIREBASE_DATA, true)
            dataStoreManager.edit(DataStoreKeys.S_USER_ADID, adId)
            dataStoreManager.edit(DataStoreKeys.I_CREATABLE_CNT, creatableCnt)
        }
    }

    private suspend fun fetchCreatableCnt(adId: String): Result<Int> = runCatching {
        (firebaseDatabase.getUserReference(adId)
            .get()
            .await()
            .value as Long).toInt()
    }

    private suspend fun postUserAdId(adId: String): Result<Int> = runCatching {
        firebaseDatabase.getUserReference(adId)
            .setValue(dataStoreManager.getValue<Int>(DataStoreKeys.I_CREATABLE_CNT) ?: DEFAULT_CREATABLE_CNT)
            .await()
    }.mapCatching { dataStoreManager.getValue<Int>(DataStoreKeys.I_CREATABLE_CNT) ?: DEFAULT_CREATABLE_CNT }

    /**
     * Firebase 실패해도 로컬 데이터에서는 처리되도록 설정
     * */
    override suspend fun postDecreaseCreatableCnt(): Result<Void> = postCreatableCnt { it - 1 }

    private suspend fun postCreatableCnt(cnt: (count: Int) -> Int): Result<Void> {
        val adId = dataStoreManager.getValue<String?>(DataStoreKeys.S_USER_ADID)
            ?: return Result.failure(AdIDNotFoundException())
        val creatableCnt = dataStoreManager.getValue<Int?>(DataStoreKeys.I_CREATABLE_CNT) ?: ZERO
        val editCount = cnt(creatableCnt).coerceAtLeast(ZERO)

        Timber.d("앞으로 생성 가능 카운트는 : ${editCount}")

        dataStoreManager.edit(DataStoreKeys.I_CREATABLE_CNT, editCount)

        return kotlin.runCatching {
            firebaseDatabase
                .getUserReference(adId)
                .setValue(editCount.toLong())
                .await()
        }
    }

    companion object {
        private const val DEFAULT_CREATABLE_CNT = 3
        private const val ZERO = 0
    }
}