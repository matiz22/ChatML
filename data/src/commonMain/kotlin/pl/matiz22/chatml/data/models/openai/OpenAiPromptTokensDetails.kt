package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiPromptTokensDetails(
    @SerialName("audio_tokens")
    val audioTokens: Int,
    @SerialName("cached_tokens")
    val cachedTokens: Int,
)
