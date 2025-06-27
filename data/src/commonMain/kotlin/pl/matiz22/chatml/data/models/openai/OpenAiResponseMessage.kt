package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiResponseMessage(
    @SerialName("content")
    val content: String,
    @SerialName("role")
    val role: String,
)
