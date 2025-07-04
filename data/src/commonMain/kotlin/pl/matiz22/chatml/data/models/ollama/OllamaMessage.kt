package pl.matiz22.chatml.data.models.ollama

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.models.TypedContent
import pl.matiz22.chatml.domain.models.TypedMessage

@Serializable
internal data class OllamaMessage(
    @SerialName("content")
    val content: String,
    @SerialName("images")
    val images: List<String>? = null,
    @SerialName("role")
    val role: String,
) {
    internal fun <T> toMessages(serializer: KSerializer<T>): List<TypedMessage<T>> {
        val textContent = Json.decodeFromString(serializer, this.content)
        val images =
            this.images?.map { img ->
                TypedMessage<T>(
                    role = Role.valueOf(this.role),
                    content = TypedContent.Image(url = img),
                )
            }
        return listOf(
            TypedMessage(
                content = TypedContent.Tool(value = textContent),
                role = Role.valueOf(this.role.uppercase()),
            ),
            *(images?.toTypedArray() ?: emptyArray()),
        )
    }

    internal fun toMessages(): List<Message> {
        val textContent =
            Message(
                content = Content.Text(text = this.content),
                role = Role.valueOf(this.role.uppercase()),
            )
        val images =
            this.images?.map { img ->
                Message(
                    role = Role.valueOf(this.role),
                    content = Content.Image(url = img),
                )
            }
        return listOf(
            textContent,
            *(images?.toTypedArray() ?: emptyArray()),
        )
    }
}
