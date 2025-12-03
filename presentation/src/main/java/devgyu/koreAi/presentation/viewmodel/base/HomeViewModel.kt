package devgyu.koreAi.presentation.viewmodel.base

import android.app.Application
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import devgyu.koreAi.core.ioDispatcher
import devgyu.koreAi.domain.entity.FluxImageRes
import devgyu.koreAi.domain.usecase.DeleteGeneratedImageDataUseCase
import devgyu.koreAi.domain.usecase.DownloadImageUrlToFileUseCase
import devgyu.koreAi.domain.usecase.FetchFluxImageUseCase
import devgyu.koreAi.domain.usecase.GetCreatableImageCountUseCase
import devgyu.koreAi.domain.usecase.GetGeneratedImageIdWithPromptListUseCase
import devgyu.koreAi.presentation.SnackBarMessages
import devgyu.koreAi.presentation.model.FluxImageStyle
import devgyu.koreAi.presentation.model.KoreAiDuration
import devgyu.koreAi.presentation.model.fluximage.FluxImageUiModel
import devgyu.koreAi.presentation.model.state.NetworkStatusTracker
import devgyu.koreAi.presentation.screen.PROMPT_MAX_LENGTH
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val fetchFluxImageUseCase: FetchFluxImageUseCase,
    private val getGeneratedImageIdWithPromptListUseCase: GetGeneratedImageIdWithPromptListUseCase,
    private val deleteGeneratedImageDataUseCase: DeleteGeneratedImageDataUseCase,
    private val getCreatableImageCountUseCase: GetCreatableImageCountUseCase,
    private val downloadImageUrlToFileUseCase: DownloadImageUrlToFileUseCase
): BaseAndroidViewModel(application) {
    private val _fluxImageModel = MutableStateFlow(FluxImageUiModel())
    val fluxImageModel = _fluxImageModel.asStateFlow()
    private val _showPreviousPrompt = MutableStateFlow(false)
    val showPreviousPrompt = _showPreviousPrompt.asStateFlow()
    private val _fluxImageResponse = MutableStateFlow<FluxImageRes?>(null)
    val fluxImageResponse = _fluxImageResponse.asStateFlow()

    val generatedImageIdWithPromptList = getGeneratedImageIdWithPromptListUseCase()
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
    private val creatableImageCount = getCreatableImageCountUseCase()
        .flowOn(ioDispatcher)
        .stateIn(viewModelScope, SharingStarted.Eagerly, 0)

    val adsChannelFlow = Channel<Boolean>(
        capacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    fun createFluxImage(){
        val prompt = _fluxImageModel.value.prompt

        when {
            prompt.isBlank() -> {
                updateSnackbarMessage(message = SnackBarMessages.ENTER_GENERATION_SENTENCE.stringRes)
                return
            }

            NetworkStatusTracker.networkConnected.value != true -> {
                updateSnackbarMessage(message = SnackBarMessages.CHECK_NETWORK.getString(application))
                return
            }
        }

        when {
            creatableImageCount.value <= 0 -> {
                viewModelScope.launch(ioDispatcher) {
                    adsChannelFlow.send(true)
                }
            }
            else -> callCreateImageApi()
        }
    }

    fun callCreateImageApi() {
        viewModelScope.launch(ioDispatcher) {
            isLoading.update { true }

            fetchFluxImageUseCase(fluxImageModel.value.toDomain(application))
                .onSuccess { res ->
                    if(res.hasNSFWConcepts){
                        updateSnackbarMessage(
                            message = SnackBarMessages.INAPPROPRIATE_WORDS_DETECTED.stringRes,
                            vibrate = true,
                            duration = KoreAiDuration.Medium
                        )
                    } else {
                        _fluxImageModel.update { it.copy(prompt = "") }
                        _fluxImageResponse.update { res }
                    }
                }.onFailure {
                    updateSnackbarMessage(
                        message = SnackBarMessages.IMAGE_GENERATION_FAILED.stringRes,
                        vibrate = true,
                        duration = KoreAiDuration.Short
                    )
                }

            isLoading.update { false }
        }
    }

    fun onShowPreviousButtonClick(){
        when {
            generatedImageIdWithPromptList.value.isNotEmpty() -> _showPreviousPrompt.update { it.not() }
            else -> updateSnackbarMessage(
                message = SnackBarMessages.NO_GENERATION_HISTORY.stringRes,
                vibrate = true,
                duration = KoreAiDuration.Short
            )
        }
    }

    fun clickGuideImage(newPrompt: String){
        _fluxImageModel.update {
            it.copy(
                selectedImageStyle = FluxImageStyle.None,
                prompt = when {
                    newPrompt.length > PROMPT_MAX_LENGTH -> newPrompt.substring(0 until PROMPT_MAX_LENGTH)
                    else -> newPrompt
                }
            )
        }
    }

    fun updatePrompt(newPrompt: String) = _fluxImageModel.update { it.copy(
        prompt = when {
            newPrompt.length > PROMPT_MAX_LENGTH -> newPrompt.substring(0 until PROMPT_MAX_LENGTH)
            else -> newPrompt
        }
    )}

    fun clearPrompt() = _fluxImageModel.update { it.copy(prompt = "") }
    fun deleteGeneratedImageData(generatedImageDataId: Long){
        viewModelScope.launch(ioDispatcher) {
            deleteGeneratedImageDataUseCase(generatedImageDataId)
        }
    }

    fun updateImageStyle(selectedImageStyle: FluxImageStyle) = _fluxImageModel.update {
        it.copy(selectedImageStyle = selectedImageStyle)
    }

    fun removeFluxImageRes() = _fluxImageResponse.update { null }

    fun onClickSaveImage(fluxImageRes: FluxImageRes){
        viewModelScope.launch(ioDispatcher) {
            isLoading.update { true }

            downloadImageUrlToFileUseCase(fluxImageRes.image.url, fluxImageRes.image.contentType)
                .onSuccess { updateSnackbarMessage(
                    SnackBarMessages.IMAGE_SAVE_SUCCESS.stringRes,
                    duration = KoreAiDuration.Short
                ) }
                .onFailure { updateSnackbarMessage(
                    SnackBarMessages.IMAGE_SAVE_FAILED.stringRes,
                    duration = KoreAiDuration.Short
                )}

            isLoading.update { false }
        }
    }
}