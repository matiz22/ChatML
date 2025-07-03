package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable

@Serializable
internal data class AnthropicFailure(
    val type: String,
    val error: AnthropicError,
)
