package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import kotlin.jvm.JvmName

@Serializable
internal data class AnthropicContent(
    val type: String,
    val text: String? = null,
    val source: Source? = null,
    val name: String? = null,
    val input: JsonElement? = null,
    val id: String? = null,
) {
    @Serializable
    data class Source
        @OptIn(ExperimentalSerializationApi::class)
        constructor(
            @EncodeDefault(EncodeDefault.Mode.ALWAYS)
            val type: String = "base64",
            val data: String? = null,
            @SerialName("media_type")
            val mediaType: String? = null,
        )

    fun toDomain(): Message =
        Message(
            role = Role.ASSISTANT,
            content = Content.Text(this.text ?: ""),
        )

    @JvmName("toDomainTool")
    fun <T> toDomain(serializer: KSerializer<T>): Message =
        if (type == "tool_use") {
            Message(
                role = Role.ASSISTANT,
                content =
                    Content.Tool(
                        value =
                            try {
                                Json.decodeFromString(
                                    serializer,
                                    input.toString(),
                                )
                            } catch (e: Exception) {
                                Content.Text(this.text ?: "")
                            },
            )
        } else {
            Message(
                role = Role.ASSISTANT,
                content = Content.Text(this.text ?: ""),
            )
        }
}
