package pl.matiz22.chatml.data.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

internal actual val IODispatcher: CoroutineDispatcher = Dispatchers.IO
