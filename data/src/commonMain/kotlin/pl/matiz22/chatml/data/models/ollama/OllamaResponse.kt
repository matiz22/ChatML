package pl.matiz22.chatml.data.models.ollama

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pl.matiz22.chatml.data.util.generateUUID
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Tokens

@Serializable
data class OllamaResponse(
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("done")
    val done: Boolean,
    @SerialName("done_reason")
    val doneReason: String? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    @SerialName("eval_duration")
    val evalDuration: Long? = null,
    @SerialName("load_duration")
    val loadDuration: Long? = null,
    @SerialName("message")
    val message: OllamaMessage,
    @SerialName("model")
    val model: String,
    @SerialName("prompt_eval_count")
    val promptEvalCount: Int? = null,
    @SerialName("prompt_eval_duration")
    val promptEvalDuration: Long? = null,
    @SerialName("total_duration")
    val totalDuration: Long? = null,
) {
    internal fun <T> toChatResponse(serializer: KSerializer<T>): ChatResponse =
        ChatResponse(
            id = generateUUID(),
            response = this.message.toMessages<T>(serializer),
            tokens =
                if (this.promptEvalCount != null && this.evalCount != null) {
                    Tokens(
                        input = this.promptEvalCount,
                        output = this.evalCount,
                    )
                } else {
                    null
                },
        )

    internal fun toChatResponse(): ChatResponse =
        ChatResponse(
            id = generateUUID(),
            response = this.message.toMessages(),
            tokens =
                if (this.promptEvalCount != null && this.evalCount != null) {
                    Tokens(
                        input = this.promptEvalCount,
                        output = this.evalCount,
                    )
                } else {
                    null
                },
        )
}
