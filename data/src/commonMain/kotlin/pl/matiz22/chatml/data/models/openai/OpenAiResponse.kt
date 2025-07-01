package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.models.Tokens

@Serializable
internal data class OpenAiResponse(
    @SerialName("choices")
    val choices: List<OpenAiChoice>,
    @SerialName("created")
    val created: Int,
    @SerialName("id")
    val id: String,
    @SerialName("model")
    val model: String,
    @SerialName("object")
    val objectX: String,
    @SerialName("system_fingerprint")
    val systemFingerprint: String?,
    @SerialName("usage")
    val usage: OpenAiUsage,
) {
    fun toMessages(): ChatResponse =
        ChatResponse(
            id = id,
            response = this.choices.toMessages(),
            tokens = Tokens(input = usage.promptTokens, output = usage.completionTokens),
        )

    private fun List<OpenAiChoice>.toMessages(): List<Message> =
        this.map { choice ->
            Message(
                role = Role.valueOf(choice.responseMessage.role.uppercase()),
                content = Content.Text(text = choice.responseMessage.content),
            )
        }

    fun <T> toMessages(serializer: KSerializer<T>): ChatResponse =
        ChatResponse(
            id = id,
            response = this.choices.toMessages(serializer),
            tokens = Tokens(input = usage.promptTokens, output = usage.completionTokens),
        )

    fun <T> List<OpenAiChoice>.toMessages(serializer: KSerializer<T>): List<Message> =
        this.map { choice ->
            try {
                val content = Json.decodeFromString(serializer, choice.responseMessage.content)
                Message(
                    role = Role.valueOf(choice.responseMessage.role.uppercase()),
                    content = Content.Tool(value = content),
                )
            } catch (e: Exception) {
                Message(
                    role = Role.valueOf(choice.responseMessage.role.uppercase()),
                    content = Content.Text(text = choice.responseMessage.content),
                )
            }
        }
}
