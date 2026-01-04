package devgyu.koreAi.presentation.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun Dp.toPx() = with(LocalDensity.current) { this@toPx.toPx() }

fun Dp.toPx(density: Density) = with(density) { this@toPx.toPx() }

@Composable
fun Int.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Int.pxToDp(density: Density) = with(density) { this@pxToDp.toDp() }

@Composable
fun Float.pxToDp() = with(LocalDensity.current) { this@pxToDp.toDp() }

fun Float.pxToDp(density: Density) = with(density) { this@pxToDp.toDp() }

@Composable
fun Dp.toImmutableDp(): TextUnit = textSp(density = LocalDensity.current)

private fun Dp.textSp(density: Density): TextUnit = with(density) {
    this@textSp.toSp()
}
