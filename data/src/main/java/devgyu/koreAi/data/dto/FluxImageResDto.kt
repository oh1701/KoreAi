package devgyu.koreAi.data.dto

import devgyu.koreAi.domain.DomainMapper
import devgyu.koreAi.domain.entity.FluxImageRes
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FluxImageResDto(
    val images: List<FluxImageDto>,
    val prompt: String,
    @SerialName("has_nsfw_concepts")
    val hasNSFWConcepts: List<Boolean>,
    val seed: Long
): DomainMapper<FluxImageRes>{
    override fun toDomain(): FluxImageRes {
        return FluxImageRes(
            image = images.map { it.toDomain() }.first(),
            prompt = prompt,
            hasNSFWConcepts = hasNSFWConcepts.first(),
            seed = seed
        )
    }
}