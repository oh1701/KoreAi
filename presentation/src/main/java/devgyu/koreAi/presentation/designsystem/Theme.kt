package devgyu.koreAi.presentation.designsystem

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val LightColorScheme = lightColorScheme(
    primary = Colors.Black,
    secondary = Colors.White,
    background = Colors.White
)

@Composable
fun GyuDefaultTheme(content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    val colorScheme = LightColorScheme

    SideEffect {
        systemUiController.setNavigationBarColor(Colors.Background)
        systemUiController.setStatusBarColor(Colors.Background)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}