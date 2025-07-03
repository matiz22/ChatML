import io.ktor.client.request.get
import io.ktor.client.statement.readRawBytes
import io.ktor.http.HttpHeaders
import io.ktor.http.Url
import io.ktor.http.isSuccess
import io.ktor.utils.io.core.toByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import pl.matiz22.chatml.data.source.httpClient
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

/**
 * A utility class to handle image URL and base64 string operations.
 * Uses Ktor client for network operations.
 */
internal class ImageProcessor {
    private val client =
        httpClient {
            expectSuccess = true
        }

    /**
     * Checks if the input string is a URL or base64 image and processes accordingly.
     * If it's a URL, downloads the image and converts it to base64.
     * If it's already base64, returns it unchanged.
     *
     * @param input The string to check (URL or base64 image)
     * @return The base64 representation of the image or null if the input is invalid
     */
    suspend fun processImageString(input: String): String? =
        when {
            isUrl(input) -> {
                try {
                    downloadImageAndConvertToBase64(input)
                } catch (e: Exception) {
                    println("Error downloading or converting image: ${e.message}")
                    null
                }
            }
            isBase64Image(input) -> {
                input
            }
            else -> {
                println("Input is neither a valid URL nor a valid base64 image")
                null
            }
        }

    /**
     * Checks if the given string is a URL.
     *
     * @param input The string to check
     * @return True if the string is a URL, false otherwise
     */
    fun isUrl(input: String): Boolean =
        try {
            Url(input)
            input.startsWith("http://") || input.startsWith("https://")
        } catch (e: Exception) {
            false
        }

    /**
     * Checks if the given string is a base64 encoded image.
     *
     * @param input The string to check
     * @return True if the string is a base64 image, false otherwise
     */
    @OptIn(ExperimentalEncodingApi::class)
    fun isBase64Image(input: String): Boolean {
        val regex = "^data:image/(jpeg|png|gif|bmp|webp);base64,[A-Za-z0-9+/=]+$"
        return input.matches(Regex(regex)) || isRawBase64Image(input)
    }

    /**
     * Checks if the string is a raw base64 string (without data URI prefix).
     *
     * @param input The string to check
     * @return True if the string is a raw base64 image, false otherwise
     */
    @OptIn(ExperimentalEncodingApi::class)
    private fun isRawBase64Image(input: String): Boolean =
        try {
            // Check if the string can be decoded as base64
            val decodedBytes = Base64.decode(input)

            // A simple heuristic: check for common image headers
            val headers =
                mapOf(
                    "JPEG" to byteArrayOf(0xFF.toByte(), 0xD8.toByte(), 0xFF.toByte()),
                    "PNG" to byteArrayOf(0x89.toByte(), 0x50.toByte(), 0x4E.toByte(), 0x47.toByte()),
                    "GIF" to "GIF8".toByteArray(),
                    "BMP" to "BM".toByteArray(),
                    "WEBP" to "RIFF".toByteArray(),
                )

            headers.values.any { header ->
                decodedBytes.size > header.size &&
                    decodedBytes.sliceArray(0 until header.size).contentEquals(header)
            }
        } catch (e: Exception) {
            false
        }

    /**
     * Downloads an image from the given URL and converts it to base64.
     * Uses Ktor client to download the image.
     *
     * @param urlString The URL of the image to download
     * @return The base64 representation of the downloaded image
     */
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun downloadImageAndConvertToBase64(urlString: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val response = client.get(urlString)

                if (response.status.isSuccess()) {
                    val imageBytes = response.readRawBytes()

                    // Determine image format from URL or content type
                    val contentType = response.headers[HttpHeaders.ContentType]
                    val format =
                        when {
                            contentType?.contains("jpeg") == true -> "jpeg"
                            contentType?.contains("jpg") == true -> "jpeg"
                            contentType?.contains("png") == true -> "png"
                            contentType?.contains("gif") == true -> "gif"
                            contentType?.contains("bmp") == true -> "bmp"
                            contentType?.contains("webp") == true -> "webp"
                            urlString.endsWith(".jpg", ignoreCase = true) -> "jpeg"
                            urlString.endsWith(".jpeg", ignoreCase = true) -> "jpeg"
                            urlString.endsWith(".png", ignoreCase = true) -> "png"
                            urlString.endsWith(".gif", ignoreCase = true) -> "gif"
                            urlString.endsWith(".bmp", ignoreCase = true) -> "bmp"
                            urlString.endsWith(".webp", ignoreCase = true) -> "webp"
                            else -> "jpeg" // Default to JPEG
                        }

                    val base64 = Base64.encode(imageBytes)
                    return@withContext "data:image/$format;base64,$base64"
                } else {
                    println("Failed to download image: ${response.status}")
                    return@withContext null
                }
            } catch (e: Exception) {
                println("Error downloading image: ${e.message}")
                return@withContext null
            }
        }

    companion object {
        /**
         * Static utility method to process an image string without creating an instance
         */
        suspend fun process(input: String): String? = ImageProcessor().processImageString(input)

        fun extractImageTypeAndBase64(input: String): Pair<String, String> {
            // Regex to match the image type and base64 string
            val regex = """(image/\w+);base64,(.*)""".toRegex()

            val matchResult = regex.find(input)
            return if (matchResult != null) {
                val imageType = matchResult.groupValues[1]
                val base64Image = matchResult.groupValues[2]
                imageType to base64Image
            } else {
                throw IllegalArgumentException("Invalid image input format")
            }
        }

        fun extractRawBase64(input: String): String {
            val prefixIndex = input.indexOf("base64,")
            return if (prefixIndex != -1) {
                input.substring(prefixIndex + "base64,".length)
            } else {
                input
            }
        }
    }
}
