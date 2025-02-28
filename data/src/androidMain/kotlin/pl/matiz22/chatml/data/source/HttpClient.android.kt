package pl.matiz22.chatml.data.source

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp

actual fun httpClient(config: HttpClientConfig<*>.() -> Unit): HttpClient =
    HttpClient(OkHttp) {
        config(this)
    }
