package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CompletionTokensDetails(
    @SerialName("accepted_prediction_tokens")
    val acceptedPredictionTokens: Int,
    @SerialName("audio_tokens")
    val audioTokens: Int? = null,
    @SerialName("reasoning_tokens")
    val reasoningTokens: Int,
    @SerialName("rejected_prediction_tokens")
    val rejectedPredictionTokens: Int,
)
