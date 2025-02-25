package pl.matiz22.chatml.domain.repository

import pl.matiz22.chatml.domain.models.ChatResponse
import pl.matiz22.chatml.domain.models.Message

interface CompletionRepository {
    fun completion(
        model: String,
        messages: List<Message>,
    ): ChatResponse
}
