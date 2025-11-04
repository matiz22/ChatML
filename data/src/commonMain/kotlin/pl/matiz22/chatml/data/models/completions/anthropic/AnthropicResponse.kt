package pl.matiz22.chatml.data.models.completions.anthropic

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.TypedChatResponse

@Serializable
internal data class AnthropicResponse(
    val id: String,
    val model: String,
    val role: String,
    @SerialName("stop_reason") val stopReason: String?,
    @SerialName("stop_sequence") val stopSequence: String?,
    val type: String = "message",
    val content: List<pl.matiz22.chatml.data.models.completions.anthropic.AnthropicContent>,
    val usage: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicUsage,
) {
    fun toDomain(): ChatResponse =
        ChatResponse(
            id = this.id,
            response =
                this.content.map { anthropicContent ->
                    anthropicContent.toDomain()
                },
            tokens = usage.toDomain(),
        )

    fun <T> toDomain(serializer: KSerializer<T>): TypedChatResponse<T> =
        TypedChatResponse(
            id = this.id,
            response =
                this.content.map { anthropicContent ->
                    anthropicContent.toDomain(serializer)
                },
            tokens = usage.toDomain(),
        )
}
