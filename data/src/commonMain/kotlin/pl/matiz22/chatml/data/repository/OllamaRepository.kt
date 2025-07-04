package pl.matiz22.chatml.data.repository

import com.xemantic.ai.tool.schema.generator.generateSchema
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import io.ktor.utils.io.readUTF8Line
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.data.models.ollama.OllamaResponse
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.data.util.sanitizeJsonSchema
import pl.matiz22.chatml.data.wrappers.prepareRequestBodyOllama
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.TypedChatResponse
import pl.matiz22.chatml.domain.repository.ChatRepository

class OllamaRepository(
    private val url: String = "http://localhost:11434/api/generate",
) : ChatRepository {
    private val client = httpClient(ollamaHttpClientConfig(url))

    override suspend fun chat(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse> =
        flow {
            val body =
                prepareRequestBodyOllama(
                    model = model,
                    messages = messages,
                    options = options,
                )

            if (options.stream) {
                val response =
                    client.post {
                        setBody(body)
                    }

                // Body as channel because sseClient does not support Text/event

                val channel = response.bodyAsChannel()
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line()
                    if (!line.isNullOrBlank()) {
                        val responseObj = Json.decodeFromString<OllamaResponse>(line)
                        emit(responseObj.toChatResponse())
                    }
                }
            } else {
                val response =
                    client.post {
                        setBody(body)
                    }
                emit(response.body<OllamaResponse>().toChatResponse())
            }
        }

    override suspend fun <T> chat(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
        serializer: KSerializer<T>,
    ): Flow<TypedChatResponse<T>> =
        flow {
            val body =
                prepareRequestBodyOllama(
                    model,
                    messages,
                    options.copy(stream = false),
                )
            val responseFormat =
                generateSchema(
                    serializer.descriptor,
                    inlineRefs = true,
                )

            val bodyWithFormat =
                body.copy(
                    format =
                        sanitizeJsonSchema(
                            Json.decodeFromString(
                                responseFormat.toString(),
                            ),
                        ),
                )

            val response =
                client.post {
                    setBody(bodyWithFormat)
                }
            val ollamaResponse: OllamaResponse = response.body()
            emit(ollamaResponse.toChatResponse(serializer))
        }

    companion object {
        private fun ollamaHttpClientConfig(baseUrl: String): HttpClientConfig<*>.() -> Unit =
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
                    requestTimeoutMillis = 1_000_000L
                    socketTimeoutMillis = 1_000_000L
                }

                install(SSE)

                defaultRequest {
                    contentType(ContentType.Application.Json)

                    val parsedUrl = Url(baseUrl)
                    url {
                        protocol = parsedUrl.protocol
                        host = parsedUrl.host
                        port = parsedUrl.port.takeIf { it != -1 } ?: parsedUrl.protocol.defaultPort
                        encodedPath = "/api/chat"
                    }
                }
            }
    }
}
