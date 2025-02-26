package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiStreamResponse(
    @SerialName("choices")
    val choices: List<StreamChoice>,
    @SerialName("created")
    val created: Int,
    @SerialName("id")
    val id: String,
    @SerialName("model")
    val model: String,
    @SerialName("object")
    val objectX: String,
    @SerialName("service_tier")
    val serviceTier: String,
    @SerialName("system_fingerprint")
    val systemFingerprint: String,
    @SerialName("usage")
    val usage: Usage,
)
