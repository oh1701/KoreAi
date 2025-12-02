package devgyu.koreAi.data.dto

import devgyu.koreAi.domain.DomainMapper
import devgyu.koreAi.domain.entity.FluxImageReq
import devgyu.koreAi.domain.model.FluxImageApiLevel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FluxImageReqDto(
    val prompt: String,
    @SerialName("image_size")
    val imageSize: FluxImageSize = FluxImageSize(),
    @SerialName("enable_safety_checker")
    val enableSafetyChecker: Boolean = true,
    @SerialName("num_inference_steps")
    val numInferenceSteps: Int = 12
): DomainMapper<FluxImageReq>{
    @Serializable
    data class FluxImageSize(
        val width: Int = 768,
        val height: Int = 768
    )

    override fun toDomain(): FluxImageReq {
        return FluxImageReq(
            prompt = prompt,
            enableSafetyChecker = enableSafetyChecker
        )
    }

    companion object {
        fun fromDomain(req: FluxImageReq): FluxImageReqDto {
            return FluxImageReqDto(
                prompt = req.prompt + req.imageStyleString + DEFAULT_PROMPT,
                enableSafetyChecker = req.enableSafetyChecker
            )
        }

        private const val DEFAULT_PROMPT = "Ultra HD, 8K render, ultra-detailed, High Quality textures"
    }
}