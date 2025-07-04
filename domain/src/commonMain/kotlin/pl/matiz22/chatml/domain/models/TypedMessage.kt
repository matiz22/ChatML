package pl.matiz22.chatml.domain.models

data class TypedMessage<T>(
    val role: Role,
    val content: TypedContent<T>,
)
