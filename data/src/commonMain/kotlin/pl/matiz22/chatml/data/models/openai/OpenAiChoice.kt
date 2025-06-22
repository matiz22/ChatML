package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiChoice(
    @SerialName("finish_reason")
    val finishReason: String,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: OpenAiLogprobs?,
    @SerialName("message")
    val responseMessage: OpenAiResponseMessage,
)
