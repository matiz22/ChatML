package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable

@Serializable
data class AnthropicError(
    val type: String,
    val message: String,
)
