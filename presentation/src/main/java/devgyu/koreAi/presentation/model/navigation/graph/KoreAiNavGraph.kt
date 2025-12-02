package devgyu.koreAi.presentation.model.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import devgyu.koreAi.presentation.model.navigation.NamedNavArguments

abstract class KoreAiNavGraph {
    // 기본 route + $argumentkey 반복
    open val arguments: List<NamedNavArguments>
        get() = emptyList()

    /* 기본 Route - 부모 클래스 이름 + 클래스 이름
    클래스 이름 하나만 사용하면 서로 다른 NavGraph 에서 동일한 name 을 가져 route 가 덮어씌워질 수도 있음 */
    val screenName: String
        get() {
            val superclassName = this::class.java.superclass.simpleName
            val className = this::class.java.simpleName
            return "$superclassName/$className"
        }

    // 지정한 argument에 맞춰서 자동적으로 route를 생성
    val route: String
        get() {
            var isAddedQueryParam = false
            return screenName + (
                    arguments
                        .sortedBy { it.isOptional }
                        .joinToString(separator = "") {
                            val isOptional = it.isOptional
                            when {
                                isOptional && !isAddedQueryParam -> {
                                    isAddedQueryParam = true
                                    "?${it.namedNavArgument.name}={${it.namedNavArgument.name}}"
                                }
                                isOptional && isAddedQueryParam ->
                                    "&${it.namedNavArgument.name}={${it.namedNavArgument.name}}"

                                else -> "/{${it.namedNavArgument.name}}"
                            }
                        }
                    )
        }

    /** createRoute 를 직접 설정하는 경우 emptyString 과 같은 상황에서 에러 발생하거나, arguments 를 빼놓는 경우가 있어 추가 */
    protected fun replaceRoute(vararg arguments: Pair<String, Any?>): String {
        var replaceRoute = route
        arguments.forEach { (name, value) -> replaceRoute = replaceRoute.replace("{$name}", value.toString()) }
        return replaceRoute
    }

    @Composable
    abstract fun NavigateScreen(navController: NavController, entry: NavBackStackEntry)
}