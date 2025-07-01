package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class AnthropicImageSource {
    @SerialName("url")
    @Serializable
    data class Url(
        val url: String,
    ) : AnthropicImageSource()

    @SerialName("base64")
    @Serializable
    data class Base64(
        @SerialName("data")
        val data: String,
        @SerialName("media_type")
        val mediaType: String? = null,
    ) : AnthropicImageSource()
}
