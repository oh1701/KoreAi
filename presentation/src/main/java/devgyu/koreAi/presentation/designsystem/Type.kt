package devgyu.koreAi.presentation.designsystem

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import devgyu.koreAi.presentation.R

object Typography {
    // 폰트
    val SpoqaHanSansNeo = FontFamily(
        Font(R.font.spoqa_han_sans_neo_bold, FontWeight.Bold, FontStyle.Normal),
        Font(R.font.spoqa_han_sans_neo_regular, FontWeight.Normal, FontStyle.Normal),
        Font(R.font.spoqa_han_sans_neo_medium, FontWeight.Medium, FontStyle.Normal),
    )

    val TitleXLB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 30.sp,
    )

    val TitleLB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 20.sp,
        lineHeight = 25.sp,
    )

    val TitleLR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 20.sp,
        lineHeight = 25.sp,
    )

    val TitleMB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 22.5.sp,
    )

    val TitleMR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp,
        lineHeight = 22.5.sp,
    )

    val TitleSR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val TitleSB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 20.sp,
    )

    val TextMB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        lineHeight = 17.5.sp
    )

    val TextMR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 17.5.sp
    )

    val TextSM = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 15.sp
    )


    val TextSB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 12.sp,
        lineHeight = 15.sp
    )

    val TextSR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 15.sp
    )

    val TextXSB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 10.sp,
        lineHeight = 12.5.sp
    )

    val TextXSM = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Medium,
        fontSize = 10.sp,
        lineHeight = 12.5.sp
    )

    val TextXSR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 10.sp,
        lineHeight = 12.5.sp,
    )

    val TextXXSB = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Bold,
        fontSize = 8.sp,
        lineHeight = 10.sp
    )

    val TextXXSR = TextStyle(
        fontFamily = SpoqaHanSansNeo,
        fontWeight = FontWeight.Normal,
        fontSize = 8.sp,
        lineHeight = 10.sp
    )
}