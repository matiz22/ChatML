package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class ToolChoice {
    @Serializable
    @SerialName("auto")
    data object Auto : ToolChoice()

    @Serializable
    @SerialName("any")
    data object Any : ToolChoice()

    @Serializable
    @SerialName("tool")
    data class SpecificTool(
        val name: String,
    ) : ToolChoice()

    @Serializable
    @SerialName("none")
    data object None : ToolChoice()
}
