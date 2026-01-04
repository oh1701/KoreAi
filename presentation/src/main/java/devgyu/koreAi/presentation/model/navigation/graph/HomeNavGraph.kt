package devgyu.koreAi.presentation.model.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import devgyu.koreAi.annotation.NavGraphRegister
import devgyu.koreAi.presentation.screen.HomeScreen

@NavGraphRegister
sealed class HomeNavGraph: KoreAiNavGraph() {
    data object Home: HomeNavGraph(){
        @Composable
        override fun NavigateScreen(navController: NavController, entry: NavBackStackEntry) {
            HomeScreen()
        }
    }
}