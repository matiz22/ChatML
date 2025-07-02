package pl.matiz22.chatml.data.models.openai

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.models.Tokens
import kotlin.jvm.JvmName

@Serializable
internal data class OpenAiStreamResponse(
    @SerialName("choices")
    val choices: List<OpenAiStreamChoice>,
    @SerialName("created")
    val created: Int,
    @SerialName("id")
    val id: String,
    @SerialName("model")
    val model: String,
    @SerialName("object")
    val objectX: String,
    @SerialName("service_tier")
    val serviceTier: String,
    @SerialName("system_fingerprint")
    val systemFingerprint: String?,
    @SerialName("usage")
    val usage: OpenAiUsage? = null,
) {
    fun toMessages(): ChatResponse =
        ChatResponse(
            id = id,
            response = this.choices.toMessages(),
            tokens =
                usage?.let {
                    Tokens(input = it.promptTokens, output = it.completionTokens)
                },
        )

    @JvmName("toMessagesFromStreamChoices")
    private fun List<OpenAiStreamChoice>.toMessages(): List<Message> =
        this.map { streamChoice ->
            Message(
                role = Role.ASSISTANT,
                content = Content.Text(text = streamChoice.delta.content ?: ""),
            )
        }
}
