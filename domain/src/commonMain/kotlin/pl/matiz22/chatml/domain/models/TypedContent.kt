package pl.matiz22.chatml.domain.models

sealed class TypedContent<out T> {
    data class Text(
        val text: String,
        val type: ContentType = ContentType.TEXT,
    ) : TypedContent<Nothing>()

    data class Image(
        val url: String,
        val type: ContentType = ContentType.IMAGE_URL,
    ) : TypedContent<Nothing>()

    data class Tool<T>(
        val value: T,
    ) : TypedContent<T>()
}
