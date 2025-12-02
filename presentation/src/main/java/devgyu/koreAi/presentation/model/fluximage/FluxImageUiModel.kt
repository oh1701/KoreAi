package devgyu.koreAi.presentation.model.fluximage

import android.app.Application
import android.content.Context
import devgyu.koreAi.domain.DomainMapper
import devgyu.koreAi.domain.entity.FluxImageReq
import devgyu.koreAi.presentation.model.FluxImageStyle
import devgyu.koreAi.presentation.model.FluxImageStyle.None

data class FluxImageUiModel(
    val prompt: String = "",
    val selectedImageStyle: FluxImageStyle = None,
) {
    private fun convertString(application: Application): String {
        return if(this.selectedImageStyle != None){
            "${application.getString(this.selectedImageStyle.stringRes)} Style, "
        } else {
            ""
        }
    }

    fun toDomain(application: Application): FluxImageReq {
        return FluxImageReq(
            prompt = prompt,
            imageStyleString = convertString(application)
        )
    }
}