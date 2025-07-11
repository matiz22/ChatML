package pl.matiz22.chatml.data.wrappers

import kotlinx.serialization.json.JsonElement
import pl.matiz22.chatml.data.models.openai.OpenAiImageUrl
import pl.matiz22.chatml.data.models.openai.OpenAiRequest
import pl.matiz22.chatml.data.models.openai.OpenAiRequestContent
import pl.matiz22.chatml.data.models.openai.OpenAiRequestMessage
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.ContentType
import pl.matiz22.chatml.domain.models.Message

internal fun Message.toOpenAiRequestMessage(): OpenAiRequestMessage =
    OpenAiRequestMessage(
        content =
            listOf(
                OpenAiRequestContent(
                    imageUrl =
                        when (val c = this.content) {
                            is Content.Image -> OpenAiImageUrl(url = c.url)
                            else -> null
                        },
                    type =
                        when (this.content) {
                            is Content.Image -> ContentType.IMAGE_URL.value
                            else -> ContentType.TEXT.value
                        },
                    text =
                        when (val c = this.content) {
                            is Content.Text -> c.text
                            else -> null
                        },
                ),
            ),
        role = this.role.value,
    )

internal fun prepareRequestBodyOpenAi(
    model: String,
    messages: List<Message>,
    options: CompletionOptions,
    schema: JsonElement? = null,
): OpenAiRequest =
    OpenAiRequest(
        messages =
            messages.map { message: Message ->
                message.toOpenAiRequestMessage()
            },
        model = model,
        stream = if (schema == null) options.stream else false,
        maxTokens = options.maxTokens,
        responseFormat = schema,
        temperature = options.temperature,
        topP = options.topP,
    )
