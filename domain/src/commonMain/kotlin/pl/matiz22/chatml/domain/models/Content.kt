package pl.matiz22.chatml.domain.models

sealed class Content {
    data class Text(
        val text: String,
        val type: ContentType = ContentType.TEXT,
    ) : Content()

    data class Image(
        val url: String,
        val type: ContentType = ContentType.IMAGE_URL,
    ) : Content()
}
