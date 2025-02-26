package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestContent(
    @SerialName("image_url")
    val imageUrl: ImageUrl? = null,
    @SerialName("text")
    val text: String? = null,
    @SerialName("type")
    val type: String,
)
