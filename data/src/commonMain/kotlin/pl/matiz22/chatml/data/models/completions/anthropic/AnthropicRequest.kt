package pl.matiz22.chatml.data.models.completions.anthropic

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AnthropicRequest
    @OptIn(ExperimentalSerializationApi::class)
    constructor(
        val model: String,
        @SerialName("max_tokens")
        @EncodeDefault(EncodeDefault.Mode.ALWAYS)
        val maxTokens: Int? = 8192,
        val messages: List<pl.matiz22.chatml.data.models.completions.anthropic.AnthropicMessage>,
        val metadata: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicMetadataObject? = null,
        @SerialName("stop_sequences")
        val stopSequences: List<String>? = null,
        val stream: Boolean? = null,
        val system: String? = null,
        val temperature: Double? = null,
        val thinking: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicThinkingConfig? = null,
        @SerialName("tool_choice")
        val toolChoice: pl.matiz22.chatml.data.models.completions.anthropic.AnthropicToolChoice? = null,
        val tools: List<pl.matiz22.chatml.data.models.completions.anthropic.AnthropicTool>? = null,
        @SerialName("top_k")
        val topK: Int? = null,
        @SerialName("top_p")
        val topP: Double? = null,
    )
