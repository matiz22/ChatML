package pl.matiz22.chatml.domain.models

data class TypedChatResponse<T>(
    val id: String,
    val response: List<TypedMessage<T>>,
    val tokens: Tokens?,
)
