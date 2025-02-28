package pl.matiz22.chatml.domain.models

data class CompletionOptions(
    val stream: Boolean = false,
    val temperature: Double = 1.0,
    val top_p: Double = 1.0,
    val outputType: OutputType = OutputType.TEXT,
    val maxTokens: Int? = null,
)
