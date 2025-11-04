package pl.matiz22.chatml.data.models.completions.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiUsage(
    @SerialName("completion_tokens")
    val completionTokens: Int,
    @SerialName("completion_tokens_details")
    val completionTokensDetails: pl.matiz22.chatml.data.models.completions.openai.OpenAiCompletionTokensDetails,
    @SerialName("prompt_tokens")
    val promptTokens: Int,
    @SerialName("prompt_tokens_details")
    val promptTokensDetails: pl.matiz22.chatml.data.models.completions.openai.OpenAiPromptTokensDetails? = null,
    @SerialName("total_tokens")
    val totalTokens: Int,
)
