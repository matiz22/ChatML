package pl.matiz22.chatml.data.models.anthropic

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Tool(
    val name: String,
    val description: String? = null,
    @SerialName("input_schema")
    val inputSchema: JsonSchema,
) {
    @Serializable
    data class JsonSchema(
        val type: String,
        val properties: Map<String, SchemaProperty>? = null,
        val required: List<String>? = null,
    )

    @Serializable
    data class SchemaProperty(
        val type: String,
        val description: String? = null,
    )
}
