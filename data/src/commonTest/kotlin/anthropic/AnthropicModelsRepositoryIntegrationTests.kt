package anthropic

import dev.scottpierce.envvar.EnvVar
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pl.matiz22.chatml.data.repository.AnthropicModelsRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class AnthropicModelsRepositoryIntegrationTests {
    private lateinit var repository: AnthropicModelsRepository

    @BeforeTest
    fun setup() {
        val apiKey: String = EnvVar.require("ANTHROPIC_API_KEY")
        repository = AnthropicModelsRepository(apiKey)
    }

    @Test
    fun testGetAvailableModels() =
        runTest {
            println("Running testGetAvailableModels")
            // When
            val modelsFlow = repository.getAvailableModels()
            val models = modelsFlow.first()

            // Then
            assertNotNull(models)
            assertTrue(models.isNotEmpty(), "Expected at least one model to be returned.")
            println("Available models: $models")
        }
}
