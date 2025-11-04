package pl.matiz22.chatml.data.models.completions.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiStreamChoice(
    @SerialName("delta")
    val delta: pl.matiz22.chatml.data.models.completions.openai.OpenAiDelta,
    @SerialName("finish_reason")
    val finishReason: String? = null,
    @SerialName("index")
    val index: Int,
    @SerialName("logprobs")
    val logprobs: pl.matiz22.chatml.data.models.completions.openai.OpenAiLogprobs? = null,
)
