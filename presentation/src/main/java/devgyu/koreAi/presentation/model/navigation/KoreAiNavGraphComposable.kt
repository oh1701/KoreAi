package devgyu.koreAi.presentation.model.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import devgyu.koreAi.presentation.model.navigation.graph.KoreAiNavGraph
import devgyu.koreAi.presentation.extensions.getSealedSubclasses
import devgyu.koreAi.presentation.screen.LocalSnackbarHostState


@OptIn(ExperimentalComposeUiApi::class)
inline fun <reified T : KoreAiNavGraph> NavGraphBuilder.KoreAiNavGraphComposable(
    navController: NavController
) {
    val clazz = T::class

    check(clazz.isSealed) { "${T::class.simpleName} is not Sealed class" }
    clazz.getSealedSubclasses().fastForEach { type ->
        composable(
            route = type.route,
            arguments = type.arguments.map { it.namedNavArgument },
            content = { entry ->
                var contentWindowInsets by remember { mutableStateOf(PaddingValues(0.dp)) }
                val transitionIsRunning by remember(this.transition.isRunning) {
                    mutableStateOf(this.transition.isRunning)
                }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInteropFilter {
                            // 화면 전환 도중에는 화면 터치 불가능하게 처리
                            return@pointerInteropFilter transitionIsRunning
                        },
                    content = {
                        contentWindowInsets = it
                        Box(
                            Modifier
                                .animateContentSize()
                                .padding(it)
                                .consumeWindowInsets(it)
                        ) {
                            type.NavigateScreen(navController, entry)
                        }
                    },
                    snackbarHost = {
                        SnackbarHost(
                            modifier = Modifier
                                .consumeWindowInsets(contentWindowInsets)
                                .imePadding(),
                            hostState = LocalSnackbarHostState.current
                        )
                    },
                )
            },
        )
    }
}