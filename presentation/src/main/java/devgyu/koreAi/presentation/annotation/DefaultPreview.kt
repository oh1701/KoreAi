package devgyu.koreAi.presentation.annotation

import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import devgyu.koreAi.presentation.designsystem.Colors

@Preview(
    name = "phone-light-mode",
    device = "spec:shape=Normal,width=360,height=800,unit=dp,dpi=480",
    showBackground = true,
    backgroundColor = 0xFFF6F6F6,
)
@Preview(name = "Foldable", device = Devices.FOLDABLE, showBackground = true, backgroundColor = 0xFFF6F6F6)
internal annotation class DefaultPreview