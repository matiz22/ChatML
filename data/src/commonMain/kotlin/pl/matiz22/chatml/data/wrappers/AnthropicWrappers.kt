package pl.matiz22.chatml.data.wrappers

import pl.matiz22.chatml.data.models.anthropic.AnthropicContent
import pl.matiz22.chatml.data.models.anthropic.AnthropicImageSource
import pl.matiz22.chatml.data.models.anthropic.AnthropicMessage
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

internal fun List<Message>.extractSystemMessage(): String =
    this
        .filter { message: Message ->
            message.role == Role.SYSTEM
        }.joinToString("\n") { message: Message ->
            when (val content = message.content) {
                is Content.Image -> {
                    content.url
                }

                is Content.Text -> {
                    content.text
                }

                is Content.Tool<*> -> {
                    throw IllegalArgumentException("Provided messages cannot contain tools in messages")
                }
            }
        }

internal fun List<Message>.toAnthropic(): List<AnthropicMessage> =
    this
        .filter { message: Message ->
            message.role != Role.SYSTEM
        }.map { message: Message ->
            AnthropicMessage(
                role = message.role.value,
                content = listOf(message.content.toAnthropic()),
            )
        }

internal fun Content.toAnthropic(): AnthropicContent =
    when (this) {
        is Content.Image -> {
            val base64Prefix = "data:"
            val imageSource =
                if (this.url.startsWith(base64Prefix)) {
                    val regex = Regex("""data:(.+?);base64,(.+)""")
                    val match =
                        regex.matchEntire(this.url)
                            ?: throw IllegalArgumentException("Invalid base64 image data URL: ${this.url}")
                    val (mediaType, base64Data) = match.destructured

                    AnthropicImageSource.Base64(
                        mediaType = mediaType,
                        data = base64Data,
                    )
                } else {
                    AnthropicImageSource.Url(this.url)
                }

            AnthropicContent.Image(source = imageSource)
        }

        is Content.Text -> {
            AnthropicContent.Text(
                this.text,
            )
        }

        is Content.Tool<*> -> {
            throw IllegalArgumentException("Provided messages cannot contain tools in messages")
        }
    }
