package pl.matiz22.chatml.domain.models

data class CompletionOptions(
    val stream: Boolean = false,
    val temperature: Double? = null,
    val topP: Double? = null,
    val maxTokens: Int? = null,
)
