package devgyu.koreAi.presentation.viewmodel.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import devgyu.koreAi.core.mainDispatcher
import devgyu.koreAi.presentation.model.KoreAiDuration
import devgyu.koreAi.presentation.model.snackbar.SnackBarInfo
import devgyu.koreAi.presentation.model.snackbar.SnackbarAppearsEvent
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

open class BaseViewModel : ViewModel() {
    val snackBarChannel = Channel<SnackBarInfo>(
        capacity = 5,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val isLoading = MutableStateFlow(false)

    /**
     * [snackBarChannel] 에 메시지와 이벤트 등을 설정하기 위한 함수
     *
     * @param message 스낵바에 표시할 메시지 (String)
     * @param vibrate 스낵바 표출 시 진동 여부
     * @param duration 스낵바 유지 시간
     * @param onSnackbarAppearsEvent 스낵바 표시 후 호출되는 이벤트
     * */
    fun updateSnackbarMessage(
        message: String,
        vibrate: Boolean = true,
        duration: KoreAiDuration = KoreAiDuration.VeryShort,
        onSnackbarAppearsEvent: (() -> Unit)? = null,
    ) {
        viewModelScope.launch(mainDispatcher) {
            snackBarChannel.send(
                SnackBarInfo(
                    message = message,
                    vibrate = vibrate,
                    duration = duration,
                    onSnackbarAppearsEvent = object : SnackbarAppearsEvent {
                        override fun event() {
                            onSnackbarAppearsEvent?.invoke()
                        }
                    },
                ),
            )
        }
    }

    override fun onCleared() {
        super.onCleared()

        snackBarChannel.close()
    }
}