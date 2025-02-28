package openai

import dev.scottpierce.envvar.EnvVar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pl.matiz22.chatml.data.repository.OpenAiRepository
import pl.matiz22.chatml.domain.models.CompletionOptions
import pl.matiz22.chatml.domain.models.Content
import pl.matiz22.chatml.domain.models.Message
import pl.matiz22.chatml.domain.models.Role
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
            // Given
            val model = "gpt-3.5-turbo"
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
            // Response should mention Paris
            assertTrue(responseText.contains("Paris", ignoreCase = true))

            // Verify tokens were counted
            assertTrue(result.tokens!!.input > 0)
            assertTrue(result.tokens!!.output > 0)
        }

    @Test
    fun testCompletionWithImageMessage() =
        runTest {
            val model = "gpt-4o"
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
            // Response should mention the Eiffel Tower
            assertTrue(responseText.contains("Eiffel", ignoreCase = true))

            // Verify tokens were counted

            assertTrue(result.tokens!!.input > 0)
            assertTrue(result.tokens!!.output > 0)
        }

    @Test
    fun testCompletionWithSystemMessage() =
        runTest {
            // Given
            val model = "gpt-4"
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
            // Given
            val model = "gpt-3.5-turbo"
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

            // The output should be constrained by token limit
            // A full essay would be much longer than 50 tokens
            assertTrue(result.tokens!!.output <= 60) // Allow a small margin over the requested limit
        }

    @Test
    fun testStreamingOption() =
        runTest {
            // Given
            val model = "gpt-3.5-turbo"
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
                println(
                    it.response
                        .first()
                        .content
                        .toString(),
                )
            }

            // Then
            assertNotNull(result)
            assertNotNull(result.id)

            // Since streaming is enabled, we're just checking that we got a valid response
            // The actual streaming behavior would need to be tested differently
            val responseText = (result.response[0].content as Content.Text).text
            assertNotNull(responseText)
        }
}
