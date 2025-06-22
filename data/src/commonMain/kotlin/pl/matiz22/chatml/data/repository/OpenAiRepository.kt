package pl.matiz22.chatml.data.repository

import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.sseSession
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.data.models.openai.OpenAiChoice
import pl.matiz22.chatml.data.models.openai.OpenAiImageUrl
import pl.matiz22.chatml.data.models.openai.OpenAiRequest
import pl.matiz22.chatml.data.models.openai.OpenAiRequestContent
import pl.matiz22.chatml.data.models.openai.OpenAiRequestMessage
import pl.matiz22.chatml.data.models.openai.OpenAiResponse
import pl.matiz22.chatml.data.models.openai.OpenAiStreamChoice
import pl.matiz22.chatml.data.models.openai.OpenAiStreamResponse
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.ContentType
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.models.Tokens
import pl.matiz22.chatml.domain.repository.CompletionRepository
import kotlin.jvm.JvmName

class OpenAiRepository(
    private val apiKey: String,
) : CompletionRepository {
    private val client = httpClient(openAiHttpClientConfig(apiKey))

    override suspend fun completion(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse> =
        flow {
            val requestBody = prepareRequestBody(model, messages, options)

            if (options.stream) {
                val session =
                    client.sseSession("chat/completions") {
                        method = HttpMethod.Post
                        setBody(requestBody)
                    }

                session.incoming.collect { event ->
                    if (event.data == "[DONE]" || event.data.isNullOrBlank()) {
                        return@collect
                    }

                    val streamResponse =
                        Json.decodeFromString<OpenAiStreamResponse>(
                            event.data ?: throw Exception("Error while handling stream: null data"),
                        )
                    val chatResponse = streamResponse.toMessages()
                    emit(chatResponse)
                }
            } else {
                val response =
                    client.post("chat/completions") {
                        setBody(requestBody)
                    }
                val openAiResponse: OpenAiResponse = response.body()
                emit(openAiResponse.toMessages())
            }
        }

    private fun prepareRequestBody(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): OpenAiRequest =
        OpenAiRequest(
            model = model,
            stream = options.stream,
            messages =
                messages.map { message: Message ->
                    message.fromDomain()
                },
            maxTokens = options.maxTokens,
        )

    private fun Message.fromDomain(): OpenAiRequestMessage =
        OpenAiRequestMessage(
            content =
                listOf(
                    OpenAiRequestContent(
                        imageUrl =
                            when (val content = this.content) {
                                is Content.Image -> OpenAiImageUrl(url = content.url)
                                else -> null
                            },
                        type =
                            when (this.content) {
                                is Content.Image -> ContentType.IMAGE_URL.value
                                is Content.Text -> ContentType.TEXT.value
                            },
                        text =
                            when (val content = this.content) {
                                is Content.Text -> content.text
                                else -> null
                            },
                    ),
                ),
            role = this.role.value,
        )

    private fun OpenAiStreamResponse.toMessages(): ChatResponse =
        ChatResponse(
            id = id,
            response = this.choices.toMessages(),
            tokens =
                usage?.let {
                    Tokens(input = it.promptTokens, output = it.completionTokens)
                },
        )

    private fun OpenAiResponse.toMessages(): ChatResponse =
        ChatResponse(
            id = id,
            response = this.choices.toMessages(),
            tokens = Tokens(input = usage.promptTokens, output = usage.completionTokens),
        )

    @JvmName("toMessagesFromStreamChoices")
    private fun List<OpenAiStreamChoice>.toMessages(): List<Message> =
        this.map { streamChoice ->
            Message(
                role = Role.ASSISTANT,
                content = Content.Text(text = streamChoice.delta.content ?: ""),
            )
        }

    private fun List<OpenAiChoice>.toMessages(): List<Message> =
        this.map { choice ->
            Message(
                role = Role.valueOf(choice.responseMessage.role.uppercase()),
                content = Content.Text(text = choice.responseMessage.content),
            )
        }

    companion object {
        private fun openAiHttpClientConfig(apiKey: String): HttpClientConfig<*>.() -> Unit =
            {
                install(ContentNegotiation) {
                    json(
                        Json {
                            prettyPrint = true
                            isLenient = true
                            ignoreUnknownKeys = true
                        },
                    )
                }
                install(HttpTimeout) {
                    requestTimeoutMillis = 30000
                }
                install(SSE)
                defaultRequest {
                    contentType(io.ktor.http.ContentType.Application.Json)
                    url {
                        protocol = URLProtocol.HTTPS
                        host = "api.openai.com"
                        path("/v1/")
                    }
                    header("Authorization", "Bearer $apiKey")
                }
            }
    }
}
