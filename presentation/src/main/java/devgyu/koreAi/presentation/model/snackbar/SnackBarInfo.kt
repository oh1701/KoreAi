package devgyu.koreAi.presentation.model.snackbar

import devgyu.koreAi.presentation.model.KoreAiDuration

data class SnackBarInfo(
    val message: String,
    val duration: KoreAiDuration = KoreAiDuration.Medium,
    val vibrate: Boolean = true,
    val time: Long = System.currentTimeMillis(), // Message, Duration 이 같아도 새로운 값으로 인식하게 하기 위함
    val onSnackbarAppearsEvent: SnackbarAppearsEvent? = null,
)