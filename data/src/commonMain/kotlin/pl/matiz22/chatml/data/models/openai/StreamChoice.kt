package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StreamChoice(
    @SerialName("delta")
    val delta: Delta,
    @SerialName("finish_reason")
    val finishReason: String? = null,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: Logprobs? = null,
)
