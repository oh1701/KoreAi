package devgyu.koreAi.presentation.screen

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import devgyu.koreAi.presentation.model.navigation.graph.HomeNavGraph
import devgyu.koreAi.presentation.navigation.generateNavGraphComposable

@Composable
fun NavigatorScreen(navController: NavHostController){
    NavHost(
        navController = navController,
        startDestination = HomeNavGraph.Home.route,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        builder = {
            generateNavGraphComposable(navController)
        }
    )
}
