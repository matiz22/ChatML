package pl.matiz22.chatml.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.matiz22.chatml.data.models.openai.models.OpenAiModelList
import pl.matiz22.chatml.data.source.httpClient
import pl.matiz22.chatml.data.source.openAiHttpClientConfig
import pl.matiz22.chatml.domain.models.Model
import pl.matiz22.chatml.domain.repository.ModelsRepository

class OpenAiModelsRepository(
    private val apiKey: String,
    private val client: HttpClient =
        httpClient(
            openAiHttpClientConfig(apiKey),
        ),
) : ModelsRepository {
    override suspend fun geAvailableModels(): Flow<List<Model>> =
        flow {
            val response = client.get("models")
            val models = response.body<OpenAiModelList>()

            emit(models.toModels())
        }
}
