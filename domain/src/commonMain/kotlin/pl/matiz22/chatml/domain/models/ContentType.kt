package pl.matiz22.chatml.domain.models

enum class ContentType(
    val value: String,
) {
    TEXT("text"),
    IMAGE_URL("image_url"),
    BASE_64("base64"),
    TOOL_USE("tool_use"),
}
