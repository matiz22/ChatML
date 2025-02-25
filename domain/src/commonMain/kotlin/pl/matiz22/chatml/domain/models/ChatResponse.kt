package pl.matiz22.chatml.domain.models

data class ChatResponse(
    val id: String,
    val response: List<Message>,
    val tokens: Tokens,
)
