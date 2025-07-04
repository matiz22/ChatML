package pl.matiz22.chatml.data.models.ollama.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaModel(
    @SerialName("details")
    val details: OllamaDetails,
    @SerialName("digest")
    val digest: String,
    @SerialName("model")
    val model: String,
    @SerialName("modified_at")
    val modifiedAt: String,
    @SerialName("name")
    val name: String,
    @SerialName("size")
    val size: Long,
)
