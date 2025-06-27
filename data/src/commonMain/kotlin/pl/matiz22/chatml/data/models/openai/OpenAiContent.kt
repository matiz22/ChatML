package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiContent(
    @SerialName("bytes")
    val bytes: List<Int>,
    @SerialName("logprob")
    val logprob: Double,
    @SerialName("token")
    val token: String,
    @SerialName("top_logprobs")
    val topLogprobs: List<OpenAiTopLogprob>,
)
