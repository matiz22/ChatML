package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable

@Serializable
internal data class AnthropicMessage(
    val role: String,
    val content: List<AnthropicContent>,
)
