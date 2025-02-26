package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Delta(
    @SerialName("content")
    val content: String,
    @SerialName("refusal")
    val refusal: String? = null,
    @SerialName("role")
    val role: String,
)
