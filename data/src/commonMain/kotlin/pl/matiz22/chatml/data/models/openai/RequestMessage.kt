package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestMessage(
    @SerialName("content")
    val content: List<RequestContent>,
    @SerialName("role")
    val role: String,
)
