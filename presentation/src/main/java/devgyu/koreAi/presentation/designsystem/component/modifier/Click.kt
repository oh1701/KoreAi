package devgyu.koreAi.presentation.designsystem.component.modifier

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.ripple
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import devgyu.koreAi.presentation.designsystem.Colors
import devgyu.koreAi.domain.util.TimeUtils


/**
 * ripple 의 색상을 고정시키는 clickable
 * @param color - ripple Color
 * @param disableRepeatedClicks - 반복 클릭 방지용 [true] : 반복 클릭 불가, [false] : 반복 클릭 허용
 * @param radius - Ripple Radius
 * @param enabled - Click 허용, 비허용
 * @param ripple - Ripple 허용
 * @param bounded - Ripple 범위가 뷰의 범위를 넘게 할 지 설정, true - 벗어나지 않음, false - 벗어남
 * @param onClickEvent - 클릭 시 발생시킬 이벤트
 * */
fun Modifier.forceColorClickable(
    isDarkColor: Boolean = false,
    color: Color = if(isDarkColor) {
        Colors.Ripple
    }else {
        Colors.RippleBlack
    },
    disableRepeatedClicks: Boolean = true,
    radius: Dp = Dp.Unspecified,
    enabled: Boolean = true,
    ripple: Boolean = true,
    bounded: Boolean = true,
    period: Long = 300,
    onClickEvent: () -> Unit,
): Modifier = composed {
    var lastClickTime by rememberSaveable { mutableLongStateOf(0L) }
    val interactionSource = remember { MutableInteractionSource() }
    val indication = ripple(
        color = color,
        bounded = bounded,
        radius = radius,
    ).takeIf { ripple }

    this.clickable(
        enabled = enabled,
        interactionSource = interactionSource,
        indication = indication,
        onClick = {
            val now = System.currentTimeMillis()
            when {
                disableRepeatedClicks && (lastClickTime + (TimeUtils.MS.MS * period) <= now) -> {
                    lastClickTime = now
                    onClickEvent()
                }

                !disableRepeatedClicks -> {
                    lastClickTime = now
                    onClickEvent()
                }

                else -> {}
            }
        },
    )
}

/**
 * Press 상태일 시, 바운스 효과를 주기 위해 만든 Modifier 속성. 클릭 효과도 가능
 * @param lazyComponentModifier - 효과들을 입힐 Modifier.
 * @param bounceScale - Press 상태에서 Scale 크기
 * @param finishedListener - Scale Animation 이 끝나고 실행할 Listener
 * @see forceColorClickable - 나머지는 이것과 속성이 같음
 * */
fun Modifier.bounceOnPressWithClickable(
    bounceModifier: Modifier,
    bounceScale: Float = 0.9f,
    color: Color = Colors.Ripple,
    disableRepeatedClicks: Boolean = true,
    radius: Dp = Dp.Unspecified,
    enabled: Boolean = true,
    ripple: Boolean = true,
    bounded: Boolean = true,
    period: Long = 300,
    finishedListener: ((Float) -> Unit)? = null,
    onClickEvent: () -> Unit,
) = composed {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) bounceScale else 1f,
        label = "",
        finishedListener = finishedListener,
    )
    val indication = ripple(
        color = color,
        bounded = bounded,
        radius = radius,
    ).takeIf { ripple }

    this
        .scale(scale)
        .then(bounceModifier)
        .clickable(
            enabled = enabled,
            interactionSource = interactionSource,
            indication = indication,
            onClick = {
                val now = System.currentTimeMillis()
                when {
                    disableRepeatedClicks && (lastClickTime + (TimeUtils.MS.MS * period) <= now) -> {
                        onClickEvent()
                        lastClickTime = now
                    }

                    !disableRepeatedClicks -> {
                        onClickEvent()
                        lastClickTime = now
                    }

                    else -> {}
                }
            },
        )
}