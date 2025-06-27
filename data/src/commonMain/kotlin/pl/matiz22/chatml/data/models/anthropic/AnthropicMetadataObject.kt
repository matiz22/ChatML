package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnthropicMetadataObject(
    @SerialName("user_id")
    val userId: String? = null,
)
