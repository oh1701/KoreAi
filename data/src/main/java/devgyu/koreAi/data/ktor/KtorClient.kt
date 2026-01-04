package devgyu.koreAi.data.ktor

import android.content.Context
import devgyu.koreAi.data.BuildConfig
import devgyu.koreAi.domain.AppBuildConfig
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import javax.inject.Inject

internal class KtorClient @Inject constructor(context: Context) {
    var ktorClient = HttpClient(OkHttp) {
        defaultRequest {
            contentType(ContentType.Application.Json)
            header("Authorization", "Key ${BuildConfig.FAL_API_KEY}")
            url(AppBuildConfig.FLUX_BASE_URL)
        }
        HttpResponseValidator {
            validateResponse { response ->
                if (response.status.value in 200..299 && response.status.value == 304) {
                    return@validateResponse
                }
            }
        }
        defaultInstalls(context)
    }
}