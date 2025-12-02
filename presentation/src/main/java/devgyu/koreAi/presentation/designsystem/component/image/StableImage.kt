package devgyu.koreAi.presentation.designsystem.component.image

import android.graphics.BitmapFactory
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun StableImage(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    contentScale: ContentScale = ContentScale.Fit,
    description: String?,
) {
    val painter = painterResource(id = drawableRes)

    Image(
        modifier = modifier,
        painter = painter,
        contentScale = contentScale,
        contentDescription = description,
        alpha = alpha,
        colorFilter = colorFilter,
    )
}