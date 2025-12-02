package devgyu.koreAi.data.dto

import devgyu.koreAi.domain.DomainMapper
import devgyu.koreAi.domain.entity.FluxImage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FluxImageDto(
    val url: String,
    @SerialName("content_type")
    val contentType: String?
): DomainMapper<FluxImage>{
    override fun toDomain(): FluxImage {
        return FluxImage(
            url = url,
            contentType = contentType
        )
    }
}
