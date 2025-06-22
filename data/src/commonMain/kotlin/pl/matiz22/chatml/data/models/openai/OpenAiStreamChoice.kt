package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiStreamChoice(
    @SerialName("delta")
    val delta: OpenAiDelta,
    @SerialName("finish_reason")
    val finishReason: String? = null,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: OpenAiLogprobs? = null,
)
