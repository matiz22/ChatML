package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
internal class AnthropicTool(
    val name: String,
    val description: String,
    @SerialName("input_schema")
    val inputSchema: JsonElement,
)
