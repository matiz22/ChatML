package pl.matiz22.chatml.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.KSerializer
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message

interface CompletionRepository {
    suspend fun completion(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
    ): Flow<ChatResponse>

    suspend fun <T> completion(
        model: String,
        messages: List<Message>,
        options: CompletionOptions,
        serializer: KSerializer<T>,
    ): Flow<ChatResponse>
}
