package pl.matiz22.chatml.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.matiz22.chatml.data.models.ollama.models.OllamaModelsList
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.data.source.ollamaHttpClientConfig
import pl.matiz22.chatml.domain.models.Model
import pl.matiz22.chatml.domain.repository.ModelsRepository

class OllamaModelsRepository(
    private val url: String = "http://localhost:11434/api/generate",
    private val client: HttpClient =
        httpClient(
            ollamaHttpClientConfig(url),
        ),
) : ModelsRepository {
    override suspend fun getAvailableModels(): Flow<List<Model>> =
        flow {
            val response = client.get("tags")
            val models = response.body<OllamaModelsList>()

            emit(models.toModels())
        }
}
