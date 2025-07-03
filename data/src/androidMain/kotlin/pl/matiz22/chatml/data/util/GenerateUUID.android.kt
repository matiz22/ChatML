package pl.matiz22.chatml.data.util

actual fun generateUUID(): String =
    java.util.UUID
        .randomUUID()
        .toString()
