package pl.matiz22.chatml.data.models.openai.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OpenAiModelData(
    @SerialName("created")
    val created: Int,
    @SerialName("id")
    val id: String,
    @SerialName("object")
    val objectX: String,
    @SerialName("owned_by")
    val ownedBy: String,
)
