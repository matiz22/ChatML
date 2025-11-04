package pl.matiz22.chatml.data.models.completions.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiChoice(
    @SerialName("finish_reason")
    val finishReason: String,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: pl.matiz22.chatml.data.models.completions.openai.OpenAiLogprobs?,
    @SerialName("message")
    val responseMessage: pl.matiz22.chatml.data.models.completions.openai.OpenAiResponseMessage,
)
