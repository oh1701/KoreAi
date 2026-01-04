package devgyu.koreAi.domain.entity

data class FluxImageRes(
    val image: FluxImage,
    val prompt: String,
    val hasNSFWConcepts: Boolean,
    val seed: Long
)