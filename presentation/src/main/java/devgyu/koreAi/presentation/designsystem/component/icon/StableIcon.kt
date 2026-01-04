package devgyu.koreAi.presentation.designsystem.component.icon

import androidx.annotation.DrawableRes
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource


@Composable
fun StableIcon(
    modifier: Modifier = Modifier,
    @DrawableRes drawableRes: Int,
    description: String,
    tint: Color = Color.Unspecified,
) {
    val painter = painterResource(id = drawableRes)

    Icon(
        modifier = modifier,
        painter = painter,
        contentDescription = description,
        tint = tint,
    )
}