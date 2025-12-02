package devgyu.koreAi.presentation

import android.app.Application

enum class SnackBarMessages(val stringRes: Int) {
    ENTER_GENERATION_SENTENCE(R.string.snackbar_enter_generation_sentence),
    CHECK_NETWORK(R.string.snackbar_check_network),
    NO_GENERATION_HISTORY(R.string.snackbar_no_generation_history),
    IMAGE_GENERATION_FAILED(R.string.snackbar_image_generation_failed),
    IMAGE_SAVE_SUCCESS(R.string.snackbar_image_save_success),
    IMAGE_SAVE_FAILED(R.string.snackbar_image_save_failed),
    INAPPROPRIATE_WORDS_DETECTED(R.string.snackbar_inappropriate_image_detected);

    fun getString(application: Application): String = application.getString(this.stringRes)
}