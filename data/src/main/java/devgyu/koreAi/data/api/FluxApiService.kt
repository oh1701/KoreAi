package devgyu.koreAi.data.api

import android.content.ContentValues
import android.content.Context
import android.provider.MediaStore
import com.google.cloud.translate.Translate
import com.google.cloud.translate.v3.DetectLanguageRequest
import dagger.Lazy
import dagger.hilt.android.qualifiers.ApplicationContext
import devgyu.koreAi.data.dto.FluxImageReqDto
import devgyu.koreAi.data.dto.FluxImageResDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.readBytes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import timber.log.Timber
import java.io.OutputStream
import javax.inject.Inject

class FluxApiService @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
    private val ktorClient: HttpClient,
    private val translateService: Lazy<Translate>
) {
    suspend fun createFluxImage(reqDto: FluxImageReqDto, apiUrl: String): FluxImageResDto {
        val translationService = translateService.get()
        val detection = translationService.detect(reqDto.prompt)
        val detectedLanguage = detection.language
        val newPrompt = if(detectedLanguage == "en"){
            Timber.d("영어만 감지")
            reqDto.prompt
        }else{
            Timber.d("기타 언어 감지")
            translationService.translate(reqDto.prompt).translatedText
        }

        Timber.d("전송 프롬포트 : ${newPrompt}")

        return ktorClient.post(apiUrl) {
            setBody(reqDto.copy(prompt = newPrompt))
        }.body()
    }

    suspend fun downloadFluxImage(imageUrl: String, contentType: String?): Result<Boolean> {
        val imageByte = HttpClient(OkHttp).get(imageUrl).readBytes()
        return saveImageToGallery(imageByte, contentType, System.currentTimeMillis().toString())
    }

    private fun saveImageToGallery(
        imageBytes: ByteArray,
        contentType: String?,
        fileName: String
    ): Result<Boolean> {
        val resolver = applicationContext.contentResolver
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, contentType ?: "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/KoreAi")
        }

        // 이미지 파일 저장 경로를 ContentResolver에 삽입하여 OutputStream을 얻음
        val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return if (imageUri != null) {
            // OutputStream을 사용해 파일로 저장
            val outputStream: OutputStream? = resolver.openOutputStream(imageUri)
            outputStream?.use { stream ->
                stream.write(imageBytes) // 이미지 바이트 배열을 파일로 씀
            }
            Result.success(true)
        } else {
            Result.failure(Exception("파일 저장 실패"))
        }
    }
}