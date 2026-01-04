package devgyu.koreAi.presentation.designsystem.component.image

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter


@Composable
fun StableAsyncImage(
    model: String?,
    @DrawableRes previewModel: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    val errorImgRes = rememberAsyncImagePainter(model = error)
    val placeHolder = rememberAsyncImagePainter(model = placeholder)

    if (LocalInspectionMode.current) {
        StableImage(
            modifier = modifier,
            description = contentDescription,
            drawableRes = previewModel,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    } else {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = modifier,
            placeholder = placeHolder,
            error = errorImgRes,
            onLoading = { onLoading?.invoke() },
            onSuccess = { onSuccess?.invoke() },
            onError = { onError?.invoke() },
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
    }
}

@Composable
fun StableAsyncImage(
    @DrawableRes model: Int?,
    @DrawableRes previewModel: Int,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    @DrawableRes placeholder: Int? = null,
    @DrawableRes error: Int? = null,
    onLoading: (() -> Unit)? = null,
    onSuccess: (() -> Unit)? = null,
    onError: (() -> Unit)? = null,
    alignment: Alignment = Alignment.Center,
    contentScale: ContentScale = ContentScale.Fit,
    alpha: Float = DefaultAlpha,
    colorFilter: ColorFilter? = null,
    filterQuality: FilterQuality = DrawScope.DefaultFilterQuality,
) {
    val localError = rememberAsyncImagePainter(model = error)
    val placeHolder = rememberAsyncImagePainter(model = placeholder)

    if (LocalInspectionMode.current) {
        StableImage(
            modifier = modifier,
            description = contentDescription,
            drawableRes = previewModel,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
        )
    } else {
        AsyncImage(
            model = model,
            contentDescription = contentDescription,
            modifier = modifier,
            placeholder = placeHolder,
            error = localError,
            onLoading = { onLoading?.invoke() },
            onSuccess = { onSuccess?.invoke() },
            onError = { onError?.invoke() },
            alignment = alignment,
            contentScale = contentScale,
            alpha = alpha,
            colorFilter = colorFilter,
            filterQuality = filterQuality,
        )
    }
}