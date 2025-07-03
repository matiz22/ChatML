package pl.matiz22.chatml.data.source

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO

internal actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(CIO) {
        config(this)
    }
