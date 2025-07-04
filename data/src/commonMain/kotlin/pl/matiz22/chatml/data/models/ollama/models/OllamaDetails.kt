package pl.matiz22.chatml.data.models.ollama.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaDetails(
    @SerialName("families")
    val families: List<String>,
    @SerialName("family")
    val family: String,
    @SerialName("format")
    val format: String,
    @SerialName("parameter_size")
    val parameterSize: String,
    @SerialName("parent_model")
    val parentModel: String,
    @SerialName("quantization_level")
    val quantizationLevel: String,
)
