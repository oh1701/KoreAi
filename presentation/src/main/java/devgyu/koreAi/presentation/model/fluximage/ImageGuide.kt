package devgyu.koreAi.presentation.model.fluximage

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import devgyu.koreAi.presentation.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Immutable
data class ImageGuide(
    @StringRes val promptRes: Int,
    @DrawableRes val drawableRes: Int,
){
    companion object {
        fun imageGuideList(): ImmutableList<ImageGuide> {
            return listOf(
                ImageGuide(
                    promptRes = R.string.image_guide_3,
                    drawableRes = R.drawable.image_guide_3
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_4,
                    drawableRes = R.drawable.image_guide_4
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_5,
                    drawableRes = R.drawable.image_guide_5
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_6,
                    drawableRes = R.drawable.image_guide_6
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_7,
                    drawableRes = R.drawable.image_guide_7
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_8,
                    drawableRes = R.drawable.image_guide_8
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_9,
                    drawableRes = R.drawable.image_guide_9
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_10,
                    drawableRes = R.drawable.image_guide_10
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_11,
                    drawableRes = R.drawable.image_guide_11
                ),
                ImageGuide(
                    promptRes = R.string.image_guide_12,
                    drawableRes = R.drawable.image_guide_12
                )
            ).shuffled().toImmutableList()
        }
    }
}