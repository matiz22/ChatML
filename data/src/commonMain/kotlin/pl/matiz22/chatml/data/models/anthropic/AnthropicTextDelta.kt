package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnthropicTextDelta(
    @SerialName("text")
    val text: String,
    @SerialName("type")
    val type: String,
)
