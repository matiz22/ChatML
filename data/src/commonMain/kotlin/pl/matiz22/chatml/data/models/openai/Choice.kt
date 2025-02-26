package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Choice(
    @SerialName("finish_reason")
    val finishReason: String,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: Logprobs?,
    @SerialName("message")
    val responseMessage: ResponseMessage,
)
