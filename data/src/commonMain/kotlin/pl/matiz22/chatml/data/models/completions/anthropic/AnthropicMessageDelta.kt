package pl.matiz22.chatml.data.models.completions.anthropic

import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

@Serializable
internal data class AnthropicMessageDelta(
    val type: String,
    val delta: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicDelta,
    val usage: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicUsage = _root_ide_package_.pl.matiz22.chatml.data.models.completions.anthropic.AnthropicUsage(),
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
