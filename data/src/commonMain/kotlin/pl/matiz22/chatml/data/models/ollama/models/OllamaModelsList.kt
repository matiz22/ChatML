package pl.matiz22.chatml.data.models.ollama.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.Model

@Serializable
data class OllamaModelsList(
    @SerialName("models")
    val models: List<OllamaModel>,
) {
    fun toModels(): List<Model> =
        models.map { model ->
            Model(
                name = model.name,
            )
        }
}
