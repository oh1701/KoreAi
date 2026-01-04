package devgyu.koreAi.domain.entity

import devgyu.koreAi.domain.model.FluxImageApiLevel

data class FluxImageReq(
    val prompt: String,
    val imageStyleString: String = "",
    val enableSafetyChecker: Boolean = true
)