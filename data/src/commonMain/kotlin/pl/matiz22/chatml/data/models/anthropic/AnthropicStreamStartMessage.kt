package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnthropicStreamStartMessage(
    val id: String,
    val type: String,
    val role: String,
    val model: String,
    val content: List<AnthropicContent>,
    @SerialName("stop_reason")
    val stopReason: String?,
    @SerialName("stop_sequence")
    val stopSequence: String?,
    val usage: AnthropicUsage,
)
