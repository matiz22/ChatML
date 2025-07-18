package pl.matiz22.chatml.data.repository

import com.xemantic.ai.tool.schema.generator.generateSchema
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.sse.sseSession
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.HttpMethod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import pl.matiz22.chatml.data.models.openai.OpenAiResponse
import pl.matiz22.chatml.data.models.openai.OpenAiStreamResponse
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.data.source.openAiHttpClientConfig
import pl.matiz22.chatml.data.wrappers.prepareRequestBodyOpenAi
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.TypedChatResponse
import pl.matiz22.chatml.domain.repository.ChatRepository

class OpenAiRepository(
    private val apiKey: String,
    private val client: HttpClient =
        httpClient(
            openAiHttpClientConfig(apiKey),
        ),
) : ChatRepository {
    override suspend fun chat(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse> =
        flow {
            val requestBody = prepareRequestBodyOpenAi(model, messages, options)

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

            val openAiText =
                buildJsonObject {
                    put("type", JsonPrimitive("json_schema"))
                    put(
                        "json_schema",
                        buildJsonObject {
                            put("name", JsonPrimitive("schema"))
                            put("schema", parsed)
                        },
                    )
                }

            val body = prepareRequestBodyOpenAi(model, messages, options, openAiText)

            val response =
                client.post("chat/completions") {
                    setBody(body)
                }
            val openAiResponse: OpenAiResponse = response.body()
            val responseChoices =
                openAiResponse.toMessages(serializer)
            emit(responseChoices)
        }
}
