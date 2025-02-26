package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiRequest(
    @SerialName("messages")
    val messages: List<RequestMessage>,
    @SerialName("model")
    val model: String,
    @SerialName("n")
    val n: Int? = null,
    @SerialName("stream")
    val stream: Boolean? = null,
    @SerialName("max_completion_tokens")
    val maxTokens: Int? = null,
)
