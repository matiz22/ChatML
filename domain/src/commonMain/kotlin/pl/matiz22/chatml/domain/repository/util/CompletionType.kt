package pl.matiz22.chatml.domain.repository.util

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.serializer
import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.repository.CompletionRepository

suspend inline fun <reified T> CompletionRepository.completion(
    model: String,
    messages: List<Message>,
    options: CompletionOptions,
): Flow<ChatResponse> =
    completion(
        model = model,
        messages = messages,
        options = options,
        serializer = serializer<T>(),
    )
