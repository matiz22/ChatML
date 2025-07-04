package pl.matiz22.chatml.data.models.anthropic.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.Model

@Serializable
data class AnthropicModelList(
    @SerialName("data")
    val data: List<AnthropicModelData>,
    @SerialName("first_id")
    val firstId: String,
    @SerialName("has_more")
    val hasMore: Boolean,
    @SerialName("last_id")
    val lastId: String,
) {
    fun toModels(): List<Model> =
        data.map {
            Model(
                name = it.id,
            )
        }
}
