package pl.matiz22.chatml.domain.models

sealed class Content {
    data class Text(
        val type: ContentType = ContentType.TEXT,
        val text: String,
    ) : Content()

    data class Image(
        val type: ContentType = ContentType.IMAGE_URL,
        val url: String,
    ) : Content()
}
