package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiRequestMessage(
    @SerialName("content")
    val content: List<OpenAiRequestContent>,
    @SerialName("role")
    val role: String,
)
