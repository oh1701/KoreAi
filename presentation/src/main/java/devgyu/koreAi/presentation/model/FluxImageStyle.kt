package devgyu.koreAi.presentation.model

import android.app.Application
import android.content.Context
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import devgyu.koreAi.presentation.R

enum class FluxImageStyle(
    @StringRes
    val stringRes: Int,
    @DrawableRes
    val drawableRes: Int
) {
    None(R.string.style_none, R.drawable.img_none),
    Realistic(R.string.style_realistic, R.drawable.img_realistic),
    TwoDGameArt(R.string.style_two_d_game_art, R.drawable.img_2d_game_art),
    ThreeDAnimation(R.string.style_three_d_animation, R.drawable.img_3d_animation),
    AbstractPainting(R.string.style_abstract_painting, R.drawable.img_abstract_painting),
    JapaneseAnimation(R.string.style_japanese_animation, R.drawable.img_japanse_anime),
    Monster(R.string.style_monster, R.drawable.img_monster),
    SteamPunk(R.string.style_steam_punk, R.drawable.img_steam_punk),
    Horror(R.string.style_horror, R.drawable.img_horror_style),
    GameGraphic(R.string.style_game_graphic, R.drawable.img_hyper_realism_3d_game_graphic),
    PopArt(R.string.style_pop_art, R.drawable.img_pop_art),
    BlackAndWhite(R.string.style_black_and_white, R.drawable.img_black_and_white),
    Renaissance(R.string.style_renaissance, R.drawable.img_renaissance),
    Sketch(R.string.style_sketch, R.drawable.img_sketch),
    StreetArt(R.string.style_street_art, R.drawable.img_street_art);
}