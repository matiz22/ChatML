package pl.matiz22.chatml.data.repository

import ImageProcessor
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
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpMethod
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.data.models.anthropic.AnthropicContent
import pl.matiz22.chatml.data.models.anthropic.AnthropicContentBlockStream
import pl.matiz22.chatml.data.models.anthropic.AnthropicMessage
import pl.matiz22.chatml.data.models.anthropic.AnthropicMessageDelta
import pl.matiz22.chatml.data.models.anthropic.AnthropicRequest
import pl.matiz22.chatml.data.models.anthropic.AnthropicResponse
import pl.matiz22.chatml.data.models.anthropic.AnthropicStartStreamData
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.ContentType
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.repository.CompletionRepository

class AnthropicRepository(
    private val apiKey: String,
) : CompletionRepository {
    private val httpClient = httpClient(anthropicHttpClientConfig(apiKey))

    override suspend fun completion(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse> =
        flow {
            val systemMessage = messages.extractSystemMessage()
            val body =
                AnthropicRequest(
                    model = model,
                    messages = messages.toAnthropic(),
                    system = systemMessage,
                    stream = options.stream,
                    maxTokens = options.maxTokens,
                )
            if (options.stream) {
                val session =
                    httpClient.sseSession("messages") {
                        setBody(body)
                        method = HttpMethod.Post
                    }
                println(session)

                session.incoming.collect { event ->
                    when (event.event) {
                        "message_start" -> {
                            val messageStart =
                                Json.decodeFromString<AnthropicStartStreamData>(
                                    event.data ?: throw Exception("Error while handling stream"),
                                )
                            emit(messageStart.toDomain())
                        }
                        "content_block_start" -> {
                            val blockStart =
                                Json.decodeFromString<AnthropicContentBlockStream>(
                                    event.data ?: throw Exception("Error while handling stream"),
                                )
                            emit(blockStart.toDomain())
                        }
                        "content_block_delta" -> {
                            val blockDelta =
                                Json.decodeFromString<AnthropicContentBlockStream>(
                                    event.data ?: throw Exception("Error while handling stream"),
                                )
                            emit(blockDelta.toDomain())
                        }
                        "message_delta" -> {
                            val messageDelta =
                                Json.decodeFromString<AnthropicMessageDelta>(
                                    event.data ?: throw Exception("Error while handling stream"),
                                )
                            emit(messageDelta.toDomain())
                        }
                    }
                }
            } else {
                val response =
                    httpClient.post("messages") {
                        setBody(body)
                    }
                println(response.bodyAsText())
                emit(response.body<AnthropicResponse>().toDomain())
            }
        }

    private fun List<Message>.extractSystemMessage(): String =
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
                }
            }

    private suspend fun List<Message>.toAnthropic(): List<AnthropicMessage> =
        this
            .filter { message: Message ->
                message.role != Role.SYSTEM
            }.map { message: Message ->
                AnthropicMessage(
                    role = message.role.value,
                    content = listOf(message.content.toAnthropic()),
                )
            }

    private suspend fun Content.toAnthropic(): AnthropicContent =
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
        }

    companion object {
        private fun anthropicHttpClientConfig(apiKey: String): HttpClientConfig<*>.() -> Unit =
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
                        host = "api.anthropic.com"
                        path("/v1/")
                    }
                    header("x-api-key", apiKey)
                    header("anthropic-version", "2023-06-01")
                }
            }
    }
}
