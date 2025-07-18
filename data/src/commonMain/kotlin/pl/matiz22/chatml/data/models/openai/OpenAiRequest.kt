package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class OpenAiRequest(
    @SerialName("messages")
    val messages: List<OpenAiRequestMessage>,
    @SerialName("model")
    val model: String,
    @SerialName("n")
    val n: Int? = null,
    @SerialName("stream")
    val stream: Boolean? = null,
    @SerialName("max_completion_tokens")
    val maxTokens: Int? = null,
    @SerialName("response_format")
    val responseFormat: JsonElement? = null,
    @SerialName("temperature")
    val temperature: Double? = null,
    @SerialName("top_p")
    val topP: Double? = null,
)
