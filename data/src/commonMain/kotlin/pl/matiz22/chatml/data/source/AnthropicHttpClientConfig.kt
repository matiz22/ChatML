package pl.matiz22.chatml.data.source

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.request.header
import io.ktor.http.URLProtocol
import io.ktor.http.contentType
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun anthropicHttpClientConfig(apiKey: String): HttpClientConfig<*>.() -> Unit =
    {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                },
            )
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 1_000_000L
            socketTimeoutMillis = 1_000_000L
        }

        install(SSE)

        defaultRequest {
            contentType(io.ktor.http.ContentType.Application.Json)
            url {
                protocol = URLProtocol.HTTPS
                host = "api.anthropic.com"
                path("/v1/")
            }
            header("x-api-key", apiKey)
            header("anthropic-version", "2023-06-01")
        }
    }
