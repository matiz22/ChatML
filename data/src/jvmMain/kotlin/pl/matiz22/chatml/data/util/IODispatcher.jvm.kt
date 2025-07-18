package pl.matiz22.chatml.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

internal actual val IODispatcher: CoroutineDispatcher = Dispatchers.IO
