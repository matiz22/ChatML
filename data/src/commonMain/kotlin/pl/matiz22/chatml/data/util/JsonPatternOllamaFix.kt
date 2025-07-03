package pl.matiz22.chatml.data.util

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonPrimitive

fun sanitizeJsonSchema(schema: JsonElement): JsonElement = fixObject(schema)

private fun fixObject(element: JsonElement): JsonElement =
    when (element) {
        is JsonObject -> {
            val fixedMap =
                element.mapValues { (key, value) ->
                    when (key) {
                        "pattern" -> JsonPrimitive(anchorRegex(value.jsonPrimitive.content))
                        "properties" -> fixProperties(value)
                        else -> fixObject(value)
                    }
                }
            JsonObject(fixedMap)
        }

        is JsonArray -> JsonArray(element.map { fixObject(it) })
        else -> element
    }

private fun fixProperties(properties: JsonElement): JsonElement {
    if (properties !is JsonObject) return properties

    val fixedProps =
        properties.mapValues { (_, propDef) ->
            if (propDef is JsonObject) fixObject(propDef) else propDef
        }

    return JsonObject(fixedProps)
}

private fun anchorRegex(pattern: String): String {
    val startsWithCaret = pattern.startsWith("^")
    val endsWithDollar = pattern.endsWith("$")

    return when {
        startsWithCaret && endsWithDollar -> pattern
        startsWithCaret -> "$pattern$"
        endsWithDollar -> "^$pattern"
        else -> "^$pattern$"
    }
}
