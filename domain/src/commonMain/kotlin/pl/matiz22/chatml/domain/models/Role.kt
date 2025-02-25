package pl.matiz22.chatml.domain.models

enum class Role(
    val value: String,
) {
    SYSTEM("system"),
    USER("user"),
    ASSISTANT("assistant"),
}
