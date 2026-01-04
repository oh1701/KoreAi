package devgyu.koreAi.presentation.extensions

import devgyu.koreAi.presentation.model.navigation.graph.KoreAiNavGraph
import kotlin.reflect.KClass

/**
 * KClass 형식의 Instance 를 T 형식으로 변환해준다
 * */
fun <T : Any> KClass<T>.getObjectInstance(): T = this.objectInstance as T

/**
 * T class 의 서브 클래스들을 추출한다.
 *
 * ```
 * sealed class Nav: KoreAiNavGraph() {
 *   data object A: Nav()
 *   data object B: Nav()
 * }
 *
 * return [A, B]
 * ```
 * @see KoreAiNavGraph
 * */
fun <T : KoreAiNavGraph> KClass<T>.getSealedSubclasses(): List<T> =
    this.sealedSubclasses.map { it.getObjectInstance() }