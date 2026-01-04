package devgyu.koreAi.domain.model

enum class FluxImageApiLevel(val apiUrl: String, val numInferenceSteps: Int) {
    Schnell(apiUrl = "flux/schnell", numInferenceSteps = 12),
    Dev(apiUrl = "flux/dev", numInferenceSteps = 28)
}