package devgyu.koreAi.presentation.designsystem.component.text

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import devgyu.koreAi.presentation.designsystem.Colors
import devgyu.koreAi.presentation.designsystem.Typography


/**
 * @param modifier innerTextField Modifier
 * @param decorationBoxModifier 전체를 감싼 decorationBox Modifier
 * @param textBoxModifier placeHolder 및 innerTextField 를 감싼 Box Modifier
 * @param decorationBoxClipShape 데코레이션 박스에 클리핑 할 Shape.
 * graphic Layer 로 뷰를 자르기 때문에 기본 정의 후 Modifier 덮어씌우기 시 기본보다 Shape 가 낮으면 적용이 불가능하여 사용
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevGyuTextField(
    modifier: Modifier = Modifier,
    textBoxModifier: Modifier = Modifier,
    decorationBoxModifier: Modifier = Modifier,
    decorationBoxClipShape: Shape = RoundedCornerShape(10.dp),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    textBoxAlignment: Alignment = Alignment.TopStart,
    value: String,
    textStyle: TextStyle = Typography.TextSR,
    textColor: Color = Colors.Black,
    textAlign: TextAlign? = null,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    prefix: @Composable (RowScope.() -> Unit)? = null,
    suffix: @Composable (RowScope.() -> Unit)? = null,
    placeholder: @Composable ((defaultColor: Color, mergedTextStyle: TextStyle) -> Unit)? = null,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    maxLength: Int = Int.MAX_VALUE,
) {
    val mergedTextStyle = when (textAlign != null) {
        true -> textStyle.merge(TextStyle(color = textColor, textAlign = textAlign))
        else -> textStyle.merge(TextStyle(color = textColor))
    }

    BasicTextField(
        modifier = modifier.defaultMinSize(minHeight = 40.dp),
        value = value,
        onValueChange = { s ->
            if (s.length <= maxLength) {
                onValueChange(s)
            }
        },
        readOnly = readOnly,
        textStyle = mergedTextStyle,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation,
        onTextLayout = onTextLayout,
        maxLines = maxLines,
        singleLine = singleLine,
        decorationBox = { innerTextField ->
            DevGyuTextFieldDecorationBox(
                modifier = decorationBoxModifier,
                textBoxModifier = textBoxModifier,
                verticalAlignment = verticalAlignment,
                textBoxAlignment = textBoxAlignment,
                value = value,
                textStyle = mergedTextStyle,
                prefix = prefix,
                suffix = suffix,
                placeholder = placeholder,
                innerTextField = innerTextField,
                modifierClipShape = decorationBoxClipShape,
            )
        },
    )
}

@Composable
private fun DevGyuTextFieldDecorationBox(
    modifier: Modifier,
    @SuppressLint("ModifierParameter") textBoxModifier: Modifier,
    modifierClipShape: Shape,
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    textBoxAlignment: Alignment = Alignment.TopStart,
    value: String,
    textStyle: TextStyle,
    prefix: @Composable (RowScope.() -> Unit)? = null,
    suffix: @Composable (RowScope.() -> Unit)? = null,
    placeholder: @Composable ((defaultColor: Color, mergedTextStyle: TextStyle) -> Unit)?,
    innerTextField: @Composable () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 40.dp)
            .clip(modifierClipShape)
            .background(Colors.Divider)
            .then(modifier),
        verticalAlignment = verticalAlignment,
    ) {
        if (prefix != null) {
            prefix()
        }

        Box(modifier = textBoxModifier.weight(1f), contentAlignment = textBoxAlignment) {
            if (placeholder != null && value.isEmpty()) {
                placeholder(Colors.HintColor, textStyle)
            }

            innerTextField()
        }

        if (suffix != null) {
            suffix()
        }
    }
}