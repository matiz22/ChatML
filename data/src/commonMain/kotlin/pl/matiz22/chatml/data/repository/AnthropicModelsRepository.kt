package pl.matiz22.chatml.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.matiz22.chatml.data.models.anthropic.models.AnthropicModelList
import pl.matiz22.chatml.data.source.anthropicHttpClientConfig
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.domain.models.Model
import pl.matiz22.chatml.domain.repository.ModelsRepository

class AnthropicModelsRepository(
    private val apiKey: String,
    private val httpClient: HttpClient =
        httpClient(
            anthropicHttpClientConfig(apiKey),
        ),
) : ModelsRepository {
    override suspend fun getAvailableModels(): Flow<List<Model>> =
        flow {
            val response = httpClient.get("models")
            val models = response.body<AnthropicModelList>()

            emit(models.toModels())
        }
}
