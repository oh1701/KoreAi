package devgyu.koreAi.presentation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import devgyu.koreAi.presentation.extensions.getVibrator
import devgyu.koreAi.presentation.extensions.vibrateEffect
import devgyu.koreAi.presentation.model.snackbar.SnackBarInfo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.withTimeoutOrNull

@Composable
@Stable
fun LocalSnackBarMessage(
    snackBarHostState: SnackbarHostState,
    snackbarChannel: Channel<SnackBarInfo>,
) {
    val context = LocalContext.current
    val vibrator = remember { getVibrator(context) }

    LaunchedEffect(Unit) {
        snackbarChannel.receiveAsFlow().collectLatest { snackBar ->
            if (snackBar.message.isNotEmpty()) {
                // 만약 스낵바가 떠 있다면 취소 후 띄어줌
                snackBarHostState.currentSnackbarData?.dismiss()

                val durationMillis = snackBar.duration.millis
                if (snackBar.vibrate) {
                    vibrator.vibrateEffect()
                }

                snackBar.onSnackbarAppearsEvent?.event()
                withTimeoutOrNull(durationMillis) {
                    snackBarHostState.showSnackbar(snackBar.message)
                }
            }
        }
    }
}