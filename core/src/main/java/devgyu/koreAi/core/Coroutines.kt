package devgyu.koreAi.core

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException
import kotlin.coroutines.cancellation.CancellationException

val coroutineExceptionHandler = CoroutineExceptionHandler { _, exception ->
    when (exception) {
        is CancellationException -> {
            Timber.e("CoroutineExceptionHandler :: 코루틴이 작업 도중 취소되었습니다.")
        }

        is UnknownHostException, is ConnectException, is IOException -> {
            Timber.e("CoroutineExceptionHandler :: 네트워크 :: $exception")
            // 네트워크 관련
        }

        is NullPointerException -> {
            Timber.e("CoroutineExceptionHandler :: 레트로핏 Response, Request 관련 클래스 등을 확인해보세요.")
        }

        else -> {
            Timber.e("CoroutineExceptionHandler :: $exception")
        }
    }
}

val ioDispatcher = Dispatchers.IO + coroutineExceptionHandler
val mainDispatcher = Dispatchers.Main + coroutineExceptionHandler
val defaultDispatcher = Dispatchers.Default + coroutineExceptionHandler