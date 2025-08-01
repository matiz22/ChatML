package pl.matiz22.chatml.data.repository

import com.xemantic.ai.tool.schema.generator.generateSchema
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sseSession
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import pl.matiz22.chatml.data.models.anthropic.AnthropicContentBlockStream
import pl.matiz22.chatml.data.models.anthropic.AnthropicMessageDelta
import pl.matiz22.chatml.data.models.anthropic.AnthropicResponse
import pl.matiz22.chatml.data.models.anthropic.AnthropicStartStreamData
import pl.matiz22.chatml.data.models.anthropic.AnthropicTool
import pl.matiz22.chatml.data.models.anthropic.AnthropicToolChoice
import pl.matiz22.chatml.data.source.anthropicHttpClientConfig
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.data.wrappers.extractSystemMessage
import pl.matiz22.chatml.data.wrappers.prepareRequestBodyAnthropic
import pl.matiz22.chatml.domain.models.ChatMLException
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.TypedChatResponse
import pl.matiz22.chatml.domain.repository.ChatRepository

class AnthropicRepository(
    private val apiKey: String,
    private val httpClient: HttpClient =
        httpClient(
            anthropicHttpClientConfig(apiKey),
        ),
) : ChatRepository {
    override suspend fun chat(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse> =
        flow {
            val systemMessage = messages.extractSystemMessage()
            val body =
                prepareRequestBodyAnthropic(model, messages, systemMessage, options)
            if (options.stream) {
                val session =
                    httpClient.sseSession("messages") {
                        setBody(body)
                        method = HttpMethod.Post
                    }

                session.incoming.collect { event ->
                    when (event.event) {
                        "message_start" -> {
                            val messageStart =
                                Json.decodeFromString<AnthropicStartStreamData>(
                                    event.data ?: throw Exception("Missing event data"),
                                )
                            emit(messageStart.toDomain())
                        }

                        "content_block_start" -> {
                            val blockStart =
                                Json.decodeFromString<AnthropicContentBlockStream>(
                                    event.data ?: throw Exception("Missing event data"),
                                )
                            emit(blockStart.toDomain())
                        }

                        "content_block_delta" -> {
                            val blockDelta =
                                Json.decodeFromString<AnthropicContentBlockStream>(
                                    event.data ?: throw Exception("Missing event data"),
                                )
                            emit(blockDelta.toDomain())
                        }

                        "message_delta" -> {
                            val messageDelta =
                                Json.decodeFromString<AnthropicMessageDelta>(
                                    event.data ?: throw Exception("Missing event data"),
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
                emit(response.body<AnthropicResponse>().toDomain())
            }
        }.catch { exception ->
            throw ChatMLException(
                "Error during completion: ${exception.message}",
            )
        }

    override suspend fun <T> chat(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
        serializer: KSerializer<T>,
    ): Flow<TypedChatResponse<T>> =
        flow {
            val schemaText =
                generateSchema(
                    serializer.descriptor,
                    inlineRefs = true,
                    additionalProperties = false,
                ).toString()
            val parsed = Json.parseToJsonElement(schemaText)
            val system = messages.extractSystemMessage()
            val name = "tool"
            val anthropicTool =
                AnthropicTool(
                    name = name,
                    description = name,
                    inputSchema = parsed,
                )

            val body =
                prepareRequestBodyAnthropic(
                    model,
                    messages,
                    system,
                    options,
                    listOf(anthropicTool),
                    AnthropicToolChoice.SpecificTool(name),
                )

            val response =
                httpClient.post("messages") {
                    setBody(body)
                }
            val chatResponse = response.body<AnthropicResponse>().toDomain(serializer)
            emit(chatResponse)
        }
}
