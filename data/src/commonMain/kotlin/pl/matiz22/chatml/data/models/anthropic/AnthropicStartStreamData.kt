package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

@Serializable
internal data class AnthropicStartStreamData(
    val type: String,
    val message: AnthropicStreamStartMessage,
) {
    fun toDomain(): ChatResponse =
        ChatResponse(
            id = message.id,
            response =
                message.content
                    .map { anthropicContent ->
                        anthropicContent.toDomain()
                    }.ifEmpty {
                        listOf(Message(Role.ASSISTANT, Content.Text("")))
                    },
            tokens = message.usage.toDomain(),
        )
}
