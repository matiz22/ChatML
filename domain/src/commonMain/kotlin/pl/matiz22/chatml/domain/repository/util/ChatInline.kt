package pl.matiz22.chatml.domain.repository.util

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.serializer
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.TypedChatResponse
import pl.matiz22.chatml.domain.repository.ChatRepository

suspend inline fun <reified T> ChatRepository.chat(
    model: String,
    messages: List<Message>,
    options: CompletionOptions,
): Flow<TypedChatResponse<T>> =
    chat(
        model = model,
        messages = messages,
        options = options,
        serializer = serializer<T>(),
    )
