package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

@Serializable
internal data class AnthropicContentBlockStream
    @OptIn(ExperimentalSerializationApi::class)
    constructor(
        val type: String,
        val index: Int,
        @JsonNames("content_block", "delta")
        val contentBlock: AnthropicTextDelta,
    ) {
        fun toDomain(): ChatResponse =
            ChatResponse(
                id = "",
                response =
                    listOf(
                        Message(
                            role = Role.ASSISTANT,
                            content = Content.Text(text = contentBlock.text),
                        ),
                    ),
                tokens = null,
            )
    }
