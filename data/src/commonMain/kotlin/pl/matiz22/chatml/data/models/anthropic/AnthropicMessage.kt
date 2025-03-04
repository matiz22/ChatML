package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable

@Serializable
data class AnthropicMessage(
    val role: String,
    val content: List<AnthropicContent>,
)
