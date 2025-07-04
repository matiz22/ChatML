package ollama

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import pl.matiz22.chatml.data.repository.OllamaModelsRepository
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class OllamaModelsRepositoryIntegrationTests {
    private lateinit var repository: OllamaModelsRepository

    @BeforeTest
    fun setup() {
        repository = OllamaModelsRepository("http://localhost:11434")
    }

    @Test
    fun testGetAvailableModels() =
        runTest {
            println("Running testGetAvailableModels")
            // When
            val modelsFlow = repository.geAvailableModels()
            val models = modelsFlow.first()

            // Then
            assertNotNull(models)
            assertTrue(models.isNotEmpty(), "Expected at least one model to be returned.")
            println("Available models: $models")
        }
}
