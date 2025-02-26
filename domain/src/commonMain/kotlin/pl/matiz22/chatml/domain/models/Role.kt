package pl.matiz22.chatml.domain.models

enum class Role(
    val value: String,
) {
    SYSTEM("system"),
    DEVELOPER("developer"),
    USER("user"),
    ASSISTANT("assistant"),
}
