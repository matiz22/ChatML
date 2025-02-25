package pl.matiz22.chatml.domain.models

data class Message(
    val role: Role,
    val content: Content,
)
