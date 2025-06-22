package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal sealed class AnthropicToolChoice {
    @Serializable
    @SerialName("auto")
    data object Auto : AnthropicToolChoice()

    @Serializable
    @SerialName("any")
    data object Any : AnthropicToolChoice()

    @Serializable
    @SerialName("tool")
    data class SpecificTool(
        val name: String,
    ) : AnthropicToolChoice()

    @Serializable
    @SerialName("none")
    data object None : AnthropicToolChoice()
}
