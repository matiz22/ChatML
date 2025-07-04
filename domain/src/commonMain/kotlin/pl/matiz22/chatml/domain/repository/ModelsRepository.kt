package pl.matiz22.chatml.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.matiz22.chatml.domain.models.Model

interface ModelsRepository {
    suspend fun geAvailableModels(): Flow<List<Model>>
}
