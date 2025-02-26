package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Logprobs(
    @SerialName("content")
    val content: List<Content>,
)
