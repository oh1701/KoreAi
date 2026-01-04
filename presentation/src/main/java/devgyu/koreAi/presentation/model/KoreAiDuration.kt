package devgyu.koreAi.presentation.model

import devgyu.koreAi.domain.util.TimeUtils

@JvmInline
value class KoreAiDuration(
    val millis: Long = 1L,
){
    companion object {
        val VeryShort: KoreAiDuration = KoreAiDuration(TimeUtils.MS.SEC)
        val Short: KoreAiDuration = KoreAiDuration(TimeUtils.MS.SEC * 2)
        val Medium: KoreAiDuration = KoreAiDuration(TimeUtils.MS.SEC * 4)
        val Long: KoreAiDuration = KoreAiDuration(TimeUtils.MS.SEC * 6)
        val VeryLong: KoreAiDuration = KoreAiDuration(TimeUtils.MS.SEC * 7)
    }
}