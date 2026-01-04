package devgyu.koreAi.domain


class AdIDNotFoundException(
    override val message: String = "ADID 를 찾을 수 없음"
): Exception()