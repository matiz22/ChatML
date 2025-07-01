package openai

import dev.scottpierce.envvar.EnvVar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import models.Address
import pl.matiz22.chatml.data.repository.OpenAiRepository
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
import pl.matiz22.chatml.domain.repository.util.completionJson
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OpenAiRepositoryIntegrationTest {
    private lateinit var repository: OpenAiRepository

    @BeforeTest
    fun setup() {
        val apiKey: String = EnvVar.require("OPENAI_API_KEY")
        repository = OpenAiRepository(apiKey)
    }

    @Test
    fun testCompletionWithTextMessage() =
        runTest {
            println("Running testCompletionWithTextMessage")
            // Given
            val model = "gpt-4.1-nano"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Text("What is the capital of France?"),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 100,
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertNotNull(result.id)
            assertEquals(1, result.response.size)
            assertEquals(Role.ASSISTANT, result.response[0].role)

            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
            println(responseText)
            // Response should mention Paris
            assertTrue(responseText.contains("Paris", ignoreCase = true))

            // Verify tokens were counted
            assertTrue(result.tokens!!.input > 0)
            assertTrue(result.tokens!!.output > 0)
        }

    @Test
    fun testCompletionWithImageMessage() =
        runTest {
            println("Running testCompletionWithImageMessage")
            val model = "gpt-4.1-nano"
            val imageUrl =
                "https://upload.wikimedia.org/wikipedia/commons/thumb/8/8a/Paris_vue_d%27ensemble_tour_Eiffel.jpg/1280px-Paris_vue_d%27ensemble_tour_Eiffel.jpg"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Image(imageUrl),
                    ),
                    Message(
                        role = Role.USER,
                        content = Content.Text("What famous landmark is shown in this image?"),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 150,
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertNotNull(result.id)
            assertEquals(1, result.response.size)
            assertEquals(Role.ASSISTANT, result.response[0].role)

            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
            println(responseText)
            // Response should mention the Eiffel Tower
            assertTrue(responseText.contains("Eiffel", ignoreCase = true))

            // Verify tokens were counted

            assertTrue(result.tokens!!.input > 0)
            assertTrue(result.tokens!!.output > 0)
        }

    @Test
    fun testCompletionWithSystemMessage() =
        runTest {
            println("Running testCompletionWithSystemMessage")
            // Given
            val model = "gpt-4.1-nano"
            val messages =
                listOf(
                    Message(
                        role = Role.SYSTEM,
                        content = Content.Text("You are a helpful assistant that speaks like a pirate."),
                    ),
                    Message(
                        role = Role.USER,
                        content = Content.Text("Tell me about the weather today."),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 150,
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertNotNull(result.id)
            assertEquals(1, result.response.size)
            assertEquals(Role.ASSISTANT, result.response[0].role)

            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
            println(responseText)
            // Response should have some pirate-like language
            assertTrue(
                responseText.contains("arr", ignoreCase = true) ||
                    responseText.contains("matey", ignoreCase = true) ||
                    responseText.contains("sea", ignoreCase = true) ||
                    responseText.contains("ship", ignoreCase = true) ||
                    responseText.contains("captain", ignoreCase = true),
            )
        }

    @Test
    fun testWithMaxTokensLimit() =
        runTest {
            println("Running testWithMaxTokensLimit")
            // Given
            val model = "gpt-4.1-nano"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Text("Write a detailed essay about the history of artificial intelligence."),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 50, // Very limited tokens
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertNotNull(result.id)

            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
            println(responseText)
            // The output should be constrained by token limit
            // A full essay would be much longer than 50 tokens
            assertTrue(result.tokens!!.output <= 60) // Allow a small margin over the requested limit
        }

    @Test
    fun testStreamingOption() =
        runTest {
            println("Running testStreamingOption")
            // Given
            val model = "gpt-4.1-nano"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Text("Count from 1 to 5."),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = true,
                    maxTokens = 30,
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()
            resultFlow.collect {
                when (
                    val content =
                        it.response
                            .first()
                            .content
                ) {
                    is Content.Image -> {
                        println(content.url + " is an image URL.")
                        println(content.type.value + " is the content type.")
                    }

                    is Content.Text -> {
                        println("Response text: ${content.text}")
                    }
                }
            }

            // Then
            assertNotNull(result)
            assertNotNull(result.id)

            // Since streaming is enabled, we're just checking that we got a valid response
            // The actual streaming behavior would need to be tested differently
            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
            println(responseText)
        }

    @Test
    fun testCompletionWithBase64Image() =
        runTest {
            println("Running testCompletionWithBase64Image")
            // Given
            val model = "gpt-4.1-nano"
            val base64Image =
                @Suppress("ktlint:standard:max-line-length")
                "data:image/png;base64,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"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Image(base64Image),
                    ),
                    Message(
                        role = Role.USER,
                        content = Content.Text("What does this image represent?"),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 150,
                )

            // When
            val resultFlow = repository.completion(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertNotNull(result.id)
            assertEquals(1, result.response.size)
            assertEquals(Role.ASSISTANT, result.response[0].role)

            val responseText = (result.response[0].content as Content.Text).text
            println(responseText)
            assertNotNull(responseText)
            // Verify the response mentions something relevant to the image
            assertTrue(responseText.isNotEmpty())
        }

    @Test
    fun testGenericCompletionWithOpenAiResponse() =
        runTest {
            println("Running testGenericCompletionWithOpenAiResponse")
            // Given
            val model = "gpt-4.1-nano"
            val messages =
                listOf(
                    Message(
                        role = Role.USER,
                        content = Content.Text("Random Address in Paris null email"),
                    ),
                )
            val options =
                CompletionOptions(
                    stream = false,
                    maxTokens = 100,
                )

            // When
            val resultFlow = repository.completionJson<Address>(model, messages, options)
            val result = resultFlow.first()

            // Then
            assertNotNull(result)
            assertEquals(
                "Paris",
                result.first().city,
            )
            println("OpenAiResponse: $result")
        }
}
