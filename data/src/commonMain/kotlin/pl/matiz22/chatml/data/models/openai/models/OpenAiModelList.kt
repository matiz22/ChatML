package pl.matiz22.chatml.data.models.openai.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.Model

@Serializable
data class OpenAiModelList(
    @SerialName("data")
    val data: List<OpenAiModelData>,
    @SerialName("object")
    val objectX: String,
) {
    fun toModels(): List<Model> =
        data.map { modelData ->
            Model(
                name = modelData.id,
            )
        }
}
