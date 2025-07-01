package pl.matiz22.chatml.domain.models

enum class ContentType(
    val value: String,
) {
    TEXT("text"),
    IMAGE_URL("image_url"),
    TOOL_USE("tool_use"),
}
