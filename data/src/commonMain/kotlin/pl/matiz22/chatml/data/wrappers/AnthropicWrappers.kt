package pl.matiz22.chatml.data.wrappers

import pl.matiz22.chatml.data.models.anthropic.AnthropicContent
import pl.matiz22.chatml.data.models.anthropic.AnthropicMessage
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.ContentType
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role

fun List<Message>.extractSystemMessage(): String =
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

internal suspend fun List<Message>.toAnthropic(): List<AnthropicMessage> =
    this
        .filter { message: Message ->
            message.role != Role.SYSTEM
        }.map { message: Message ->
            AnthropicMessage(
                role = message.role.value,
                content = listOf(message.content.toAnthropic()),
            )
        }

internal suspend fun Content.toAnthropic(): AnthropicContent =
    when (this) {
        is Content.Image -> {
            val base64 = ImageProcessor.process(this.url)
            val (imageType, base64Image) =
                ImageProcessor.extractImageTypeAndBase64(
                    base64 ?: throw Exception("error while image processing"),
                )
            AnthropicContent(
                type = "image",
                source =
                    AnthropicContent.Source(
                        mediaType = imageType,
                        data = base64Image,
                    ),
            )
        }

        is Content.Text -> {
            AnthropicContent(type = ContentType.TEXT.value, this.text)
        }

        is Content.Tool<*> -> {
            throw IllegalArgumentException("Provided messages cannot contain tools in messages")
        }
    }
