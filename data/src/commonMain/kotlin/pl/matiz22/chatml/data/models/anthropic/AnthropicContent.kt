package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.models.TypedContent
import pl.matiz22.chatml.domain.models.TypedMessage
import kotlin.jvm.JvmName

@Serializable
internal sealed class AnthropicContent {
    @SerialName("text")
    @Serializable
    data class Text(
        val text: String,
    ) : AnthropicContent()

    @SerialName("image")
    @Serializable
    data class Image(
        val source: AnthropicImageSource,
    ) : AnthropicContent()

    @SerialName("tool_use")
    @Serializable
    data class ToolUse(
        val id: String,
        val name: String,
        val input: JsonElement,
    ) : AnthropicContent()

    fun toDomain(): Message =
        when (this) {
            is Image -> throw IllegalStateException("Image are not supported in Anthropic")
            is Text -> {
                Message(
                    role = Role.ASSISTANT,
                    content = Content.Text(this.text),
                )
            }

            is ToolUse -> throw IllegalStateException("Use generic toDomain() method for ToolUse")
        }

    @JvmName("toDomainTool")
    fun <T> toDomain(serializer: KSerializer<T>): TypedMessage<T> =
        when (this) {
            is Image -> throw IllegalStateException("Image are not supported in Anthropic")
            is Text -> {
                TypedMessage(
                    role = Role.ASSISTANT,
                    content = TypedContent.Text(this.text),
                )
            }

            is ToolUse -> {
                TypedMessage(
                    role = Role.ASSISTANT,
                    content =
                        TypedContent.Tool(
                            value =
                                Json.decodeFromString(
                                    serializer,
                                    this.input.toString(),
                                ),
                        ),
                )
            }
        }
}
