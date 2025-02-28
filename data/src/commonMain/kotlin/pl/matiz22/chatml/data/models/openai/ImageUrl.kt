package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ImageUrl(
    @SerialName("url")
    val url: String,
)
