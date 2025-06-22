package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.Tokens

@Serializable
internal data class AnthropicUsage(
    @SerialName("input_tokens")
    val inputTokens: Int = 0,
    @SerialName("output_tokens")
    val outputTokens: Int = 0,
    @SerialName("cache_creation_input_tokens")
    val cacheCreationInputTokens: Int = 0,
    @SerialName("cache_read_input_tokens")
    val cacheReadInputTokens: Int = 0,
    @SerialName("service_tier")
    val serviceTier: String? = null,
) {
    fun toDomain(): Tokens =
        Tokens(
            input = this.inputTokens + this.cacheReadInputTokens,
            output = this.outputTokens + this.cacheCreationInputTokens,
        )
}
