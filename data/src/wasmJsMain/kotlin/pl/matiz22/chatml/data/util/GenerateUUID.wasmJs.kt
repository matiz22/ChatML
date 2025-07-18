package pl.matiz22.chatml.data.util

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
internal actual fun generateUUID(): String = Uuid.random().toString()
