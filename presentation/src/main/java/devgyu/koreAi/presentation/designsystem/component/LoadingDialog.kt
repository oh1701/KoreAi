package devgyu.koreAi.presentation.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import devgyu.koreAi.presentation.R
import devgyu.koreAi.presentation.designsystem.Colors
import devgyu.koreAi.presentation.designsystem.Typography
import devgyu.koreAi.presentation.designsystem.component.text.ImmutableSizeText
import kotlinx.coroutines.delay

@Composable
fun LottieLoading(
    isLoading: Boolean,
    text: String = stringResource(R.string.fetchingImage)
){
    if(!isLoading) return

    val lottie by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.lottie_create_image_loading))
    var ellipsis by remember { mutableStateOf(".") }

    LaunchedEffect(true) {
        while (true){
            delay(1000L)
            when {
                ellipsis.length < 3 -> ellipsis += "."
                else -> ellipsis = "."
            }
        }
    }

    Dialog(
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
        ),
        onDismissRequest = {}
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LottieAnimation(
                modifier = Modifier.size(160.dp),
                composition = lottie,
                iterations = LottieConstants.IterateForever
            )

            ImmutableSizeText(
                text = "$text $ellipsis",
                style = Typography.TextMR,
                color = Colors.GrayWhite
            )
        }
    }
}