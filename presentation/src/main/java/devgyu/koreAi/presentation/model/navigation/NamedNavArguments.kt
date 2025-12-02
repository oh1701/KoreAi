package devgyu.koreAi.presentation.model.navigation

import androidx.compose.runtime.Stable
import androidx.navigation.NamedNavArgument

@Stable
data class NamedNavArguments(
    val namedNavArgument: NamedNavArgument,
    val isOptional: Boolean = false
)