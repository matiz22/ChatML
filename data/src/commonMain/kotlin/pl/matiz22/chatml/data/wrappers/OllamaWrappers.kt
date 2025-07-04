package pl.matiz22.chatml.data.wrappers

import pl.matiz22.chatml.data.models.ollama.OllamaMessage
import pl.matiz22.chatml.data.models.ollama.OllamaOptions
import pl.matiz22.chatml.data.models.ollama.OllamaRequest
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.ContentType
import pl.matiz22.chatml.domain.models.Message

internal suspend fun prepareRequestBodyOllama(
    model: String,
    messages: List<Message>,
    options: CompletionOptions,
): OllamaRequest =
    OllamaRequest(
        messages = messages.toOllamaMessages(),
        model = model,
        options = options.toOllamaOptions(),
        stream = options.stream,
    )

internal suspend fun List<Message>.toOllamaMessages(): List<OllamaMessage> = this.map { it.toOllamaMessage() }

internal suspend fun Message.toOllamaMessage(): OllamaMessage =
    OllamaMessage(
        content =
            when (val content = this.content) {
                is Content.Image -> ""
                is Content.Text -> content.text
            },
        images =
            when (val content = this.content) {
                is Content.Image -> {
                    if (content.type == ContentType.IMAGE_URL) {
                        // Extract only the base64 part and wrap it in a list
                        val base64 = ImageProcessor.process(content.url) ?: ""
                        listOf(ImageProcessor.extractRawBase64(base64))
                    } else {
                        listOf(content.url)
                    }
                }

                else -> null
            },
        role = this.role.value,
    )

internal fun CompletionOptions.toOllamaOptions(): OllamaOptions =
    OllamaOptions(
        topP = this.topP,
        temperature = this.temperature,
        numPredict = this.maxTokens,
    )
