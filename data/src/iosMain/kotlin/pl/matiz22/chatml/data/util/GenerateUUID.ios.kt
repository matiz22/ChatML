package pl.matiz22.chatml.data.util

import platform.Foundation.NSUUID

actual fun generateUUID(): String = NSUUID().UUIDString()
