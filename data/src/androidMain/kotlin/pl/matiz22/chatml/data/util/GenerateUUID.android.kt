package pl.matiz22.chatml.data.util

internal actual fun generateUUID(): String =
    java.util.UUID
        .randomUUID()
        .toString()
