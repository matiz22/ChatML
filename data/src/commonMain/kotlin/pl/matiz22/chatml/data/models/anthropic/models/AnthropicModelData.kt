package pl.matiz22.chatml.data.models.anthropic.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnthropicModelData(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("id")
    val id: String,
    @SerialName("type")
    val type: String,
)
