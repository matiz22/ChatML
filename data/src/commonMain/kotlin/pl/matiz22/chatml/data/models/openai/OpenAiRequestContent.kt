package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiRequestContent(
    @SerialName("image_url")
    val imageUrl: OpenAiImageUrl? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("type")
    val type: String,
)
