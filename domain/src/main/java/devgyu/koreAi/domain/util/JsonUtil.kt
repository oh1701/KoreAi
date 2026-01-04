package devgyu.koreAi.domain.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object JsonUtil {
    val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        encodeDefaults = true
        isLenient = true
        prettyPrint = true
    }

    /**
     * @param T 변환 시킬 클래스.
     * 해당 함수는 Json 을 T 클래스로 변환시키는 함수임
     * */
    inline fun <reified T> String.fromJson(): T {
        return json.decodeFromString(this)
    }

    /**
     * @param T 변환 시킬 클래스.
     * 해당 함수는 T 클래스를 Json 으로 변환시키는 함수임
     * */
    inline fun <reified T> T.toJson(): String {
        return json.encodeToString(this)
    }
}