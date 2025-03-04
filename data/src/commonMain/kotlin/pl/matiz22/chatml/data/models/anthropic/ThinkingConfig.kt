package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ThinkingConfig(
    @SerialName("budget_tokens")
    val budgetTokens: Int,
    val type: String = "enabled",
)
