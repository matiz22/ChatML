package pl.matiz22.chatml.data.source

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.sse.SSE
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun ollamaHttpClientConfig(baseUrl: String): HttpClientConfig<*>.() -> Unit =
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
            contentType(ContentType.Application.Json)

            val parsedUrl = Url(baseUrl)
            url {
                protocol = parsedUrl.protocol
                host = parsedUrl.host
                port = parsedUrl.port.takeIf { it != -1 } ?: parsedUrl.protocol.defaultPort
                encodedPath = "/api/"
            }
        }
    }
