package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiDelta(
    @SerialName("content")
    val content: String? = null,
    @SerialName("refusal")
    val refusal: String? = null,
    @SerialName("role")
    val role: String? = null,
)
