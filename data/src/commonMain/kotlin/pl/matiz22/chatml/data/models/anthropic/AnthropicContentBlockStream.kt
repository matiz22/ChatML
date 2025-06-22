package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import pl.matiz22.chatml.domain.models.ChatResponse

@Serializable
internal data class AnthropicContentBlockStream
    @OptIn(ExperimentalSerializationApi::class)
    constructor(
        val type: String,
        val index: Int,
        @JsonNames("content_block", "delta")
        val contentBlock: AnthropicContent,
    ) {
        fun toDomain(): ChatResponse =
            ChatResponse(
                id = "",
                response = listOf(contentBlock.toDomain()),
                tokens = null,
            )
    }
