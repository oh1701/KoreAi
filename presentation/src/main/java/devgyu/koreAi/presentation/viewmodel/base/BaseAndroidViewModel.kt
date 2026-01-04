package devgyu.koreAi.presentation.viewmodel.base

import android.app.Application
import androidx.lifecycle.viewModelScope
import devgyu.koreAi.core.mainDispatcher
import devgyu.koreAi.presentation.model.KoreAiDuration
import devgyu.koreAi.presentation.model.snackbar.SnackBarInfo
import devgyu.koreAi.presentation.model.snackbar.SnackbarAppearsEvent
import kotlinx.coroutines.launch

open class BaseAndroidViewModel(protected val application: Application): BaseViewModel(){
    fun updateSnackbarMessage(
        message: Int,
        vibrate: Boolean = true,
        duration: KoreAiDuration = KoreAiDuration.Short,
        onSnackbarAppearsEvent: (() -> Unit)? = null,
    ) {
        viewModelScope.launch(mainDispatcher) {
            snackBarChannel.send(
                SnackBarInfo(
                    message = application.getString(message),
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
}