package devgyu.koreAi.data.ktor.interceptor

import io.ktor.client.HttpClient
import io.ktor.client.call.HttpClientCall
import io.ktor.client.plugins.cache.storage.CachedResponseData
import io.ktor.client.request.HttpRequestPipeline
import io.ktor.client.statement.HttpReceivePipeline
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.readBytes
import io.ktor.client.statement.request
import io.ktor.http.Headers
import io.ktor.http.HttpMethod
import io.ktor.http.HttpProtocolVersion
import io.ktor.http.HttpStatusCode
import io.ktor.http.Url
import io.ktor.http.content.OutgoingContent
import io.ktor.util.InternalAPI
import io.ktor.util.date.GMTDate
import io.ktor.utils.io.ByteReadChannel
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import timber.log.Timber
import java.math.BigInteger
import java.security.MessageDigest
import java.util.concurrent.ConcurrentHashMap
import kotlin.coroutines.CoroutineContext

private const val PERIOD = 400L

/*
* Retrofit 에서는 비슷하게 적용해서 되었지만, 여기서는 왜인지 정상작동 안되어 사용하지 않은 것으로 기억
* */
@OptIn(InternalAPI::class)
internal fun HttpClient.ktorDuplicateCheckInterceptor() {
    val requestMap = ConcurrentHashMap<String, HttpResponseInfo>()
    val mutexLockMap = ConcurrentHashMap<String, Mutex>()

    requestPipeline.intercept(HttpRequestPipeline.Before) {
        val requestData = context.build()
        val hash = createHash(requestData.url, requestData.method, requestData.body)
        mutexLockMap.computeIfAbsent(hash) { Mutex() }
        val mutexLock = mutexLockMap[hash]!!

        // 중복 호출 방지. 이전 요청이 끝나야 서버에 Request
        // TODO ReentrantLock 과 속도 차이 확인
        mutexLock.withLock { proceed() }
    }

    receivePipeline.intercept(HttpReceivePipeline.Before) { response ->
        val request = response.request
        val hash = createHash(request.url, request.method, request.content)
        val requestInfo = requestMap[hash]

        when (!isTimeElapsed((requestInfo?.lastExecutionTime ?: 0L), PERIOD)) {
            true -> {
                // 캐시된 응답 반환
                val cachedResponse = requestInfo!!.response
                Timber.i("[ktorDuplicateCheckInterceptor, Cache Response]")
                proceedWith(object : HttpResponse() {
                    override val headers: Headers = cachedResponse.headers
                    override val status: HttpStatusCode = cachedResponse.statusCode
                    override val version: HttpProtocolVersion = cachedResponse.version
                    override val requestTime: GMTDate = cachedResponse.requestTime
                    override val responseTime: GMTDate = cachedResponse.responseTime
                    override val call: HttpClientCall = response.call
                    override val content: ByteReadChannel = ByteReadChannel(cachedResponse.body)
                    override val coroutineContext: CoroutineContext = response.coroutineContext
                })
            }

            else -> {
                val cacheResponse = CachedResponseData(
                    url = request.url,
                    statusCode = response.status,
                    requestTime = response.requestTime,
                    responseTime = response.responseTime,
                    version = response.version,
                    expires = GMTDate(Long.MAX_VALUE),
                    headers = response.headers,
                    varyKeys = mapOf(),
                    body = response.readBytes(),
                )
                requestMap[hash] = HttpResponseInfo(cacheResponse, System.currentTimeMillis())
                proceedWith(object : HttpResponse() {
                    override val headers: Headers = cacheResponse.headers
                    override val status: HttpStatusCode = cacheResponse.statusCode
                    override val version: HttpProtocolVersion = cacheResponse.version
                    override val requestTime: GMTDate = cacheResponse.requestTime
                    override val responseTime: GMTDate = cacheResponse.responseTime
                    override val call: HttpClientCall = response.call
                    override val content: ByteReadChannel = ByteReadChannel(cacheResponse.body)
                    override val coroutineContext: CoroutineContext = response.coroutineContext
                })
            }
        }
    }
}

private fun createHash(url: Url, method: HttpMethod, content: OutgoingContent): String {
    return sha256("$url.${method.value}.$content")
}

private fun isTimeElapsed(timestampMs: Long, intervalMs: Long): Boolean {
    return System.currentTimeMillis() - timestampMs > intervalMs
}

// sha256 암호화
private fun sha256(input: String): String {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(input.toByteArray())
    return String.format("%064x", BigInteger(1, md.digest()))
}

private data class HttpResponseInfo(
    val response: CachedResponseData,
    val lastExecutionTime: Long,
)