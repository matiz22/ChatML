package pl.matiz22.chatml.domain.repository

import pl.matiz22.chatml.domain.models.Message

interface CompletionRepository {
    fun completion(messages: List<Message>): Message
}
