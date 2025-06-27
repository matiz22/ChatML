package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class OpenAiLogprobs(
    @SerialName("content")
    val content: List<OpenAiContent>,
)
