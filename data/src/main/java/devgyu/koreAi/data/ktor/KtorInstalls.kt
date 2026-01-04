package devgyu.koreAi.data.ktor

import android.accounts.NetworkErrorException
import android.content.Context
import devgyu.koreAi.domain.util.JsonUtil
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.FileStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import timber.log.Timber
import java.io.File

internal fun HttpClientConfig<*>.defaultInstalls(context: Context) {
    /** 직렬화 **/
    install(ContentNegotiation) { json(JsonUtil.json) }

    /** 로깅 **/
    install(Logging) {
        logger = object : Logger {
            override fun log(message: String) {
                println(message)
                Timber.i(message)
            }
        }
        level = LogLevel.BODY
        sanitizeHeader { it == HttpHeaders.Authorization }
    }

    /** 재시도 **/
    // TODO 이것을 처리한다면 viewModel 의 retryIO 를 제거해도 될 듯 하다
    install(HttpRequestRetry) {
        maxRetries = 3
        retryOnExceptionIf { _, throwable -> throwable is NetworkErrorException }
        delayMillis { _ -> 3_000L }
    }

    // 타임아웃
    install(HttpTimeout){
        requestTimeoutMillis = 60_000
        socketTimeoutMillis = 60_000
        connectTimeoutMillis = 60_000
    }

    /** 캐싱 **/
    install(HttpCache) {
        val httpCacheFileStorage = FileStorage(File(context.filesDir, "ETagFile"))
        // privateStorage 는 Response 헤더에 PRIVATE 관련 키가 있어야 하는듯
        publicStorage(httpCacheFileStorage)
    }
}
