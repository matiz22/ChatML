package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

@Serializable
internal data class AnthropicMessageDelta(
    val type: String,
    val delta: AnthropicDelta,
    val usage: AnthropicUsage = AnthropicUsage(),
) {
    fun toDomain(): ChatResponse =
        ChatResponse(
            id = "",
            response =
                listOf(
                    Message(
                        role = Role.ASSISTANT,
                        content = Content.Text(""),
                    ),
                ),
            tokens = usage.toDomain(),
        )
}
