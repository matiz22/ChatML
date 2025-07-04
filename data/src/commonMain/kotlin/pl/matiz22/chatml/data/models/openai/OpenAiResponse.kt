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
import pl.matiz22.chatml.domain.models.TypedChatResponse
import pl.matiz22.chatml.domain.models.TypedContent
import pl.matiz22.chatml.domain.models.TypedMessage

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

    fun <T> toMessages(serializer: KSerializer<T>): TypedChatResponse<T> =
        TypedChatResponse(
            id = id,
            response = this.choices.toMessages(serializer),
            tokens = Tokens(input = usage.promptTokens, output = usage.completionTokens),
        )

    fun <T> List<OpenAiChoice>.toMessages(serializer: KSerializer<T>): List<TypedMessage<T>> =
        this.map { choice ->
            try {
                val content = Json.decodeFromString(serializer, choice.responseMessage.content)
                TypedMessage(
                    role = Role.valueOf(choice.responseMessage.role.uppercase()),
                    content = TypedContent.Tool(value = content),
                )
            } catch (e: Exception) {
                TypedMessage(
                    role = Role.valueOf(choice.responseMessage.role.uppercase()),
                    content = TypedContent.Text(text = choice.responseMessage.content),
                )
            }
        }
}
