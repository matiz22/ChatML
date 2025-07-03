package pl.matiz22.chatml.data.models.ollama

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal data class OllamaRequest(
    @SerialName("messages")
    val messages: List<OllamaMessage>,
    @SerialName("model")
    val model: String,
    @SerialName("options")
    val options: OllamaOptions,
    @SerialName("stream")
    val stream: Boolean,
    val format: JsonElement? = null,
)
