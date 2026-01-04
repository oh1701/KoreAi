package devgyu.koreAi.domain

interface DomainMapper<Domain> {
    fun toDomain(): Domain
}
