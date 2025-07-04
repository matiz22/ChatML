# ChatML

ChatML is a Kotlin Multiplatform Mobile (KMM) library designed to facilitate interaction with various AI-based chat services, such as OpenAI, Anthropic, and Ollama. It provides a unified interface for sending messages, receiving responses, and handling advanced features like streaming and custom serialization.

## Features

- **Unified Chat Interface**: Interact with multiple AI services using a common interface.
- **Streaming Support**: Handle real-time responses from services like OpenAI and Anthropic.
- **Custom Serialization**: Serialize and deserialize custom data types for advanced use cases.
- **Extensibility**: Easily add support for new AI services.

## Adding the Library

To use ChatML in your project, add the following to your `build.gradle.kts`:

```kotlin
repositories {
    maven {
        name = "GitHubPackages"
        url = uri("https://maven.pkg.github.com/matiz22/ChatML")
    }
}

dependencies {
    implementation("pl.matiz22.chatml:core:<latest-version>")
}
```

Replace `<latest-version>` with the newest version available.

## Repositories

### 1. `AnthropicRepository`
- **Purpose**: Handles communication with Anthropic's AI services.
- **Features**:
  - Supports both standard and streaming responses.
  - Allows custom serialization for tool-based responses.
- **Usage**:
  ```kotlin
  val repository: ChatRepository = AnthropicRepository(apiKey = "your-api-key")
  val responseFlow = repository.chat(
      model = "claude-3",
      messages = listOf(Message(role = Role.USER, content = Content.Text("Hello!"))),
      options = CompletionOptions(stream = false, maxTokens = 100)
  )
  ```

### 2. `OpenAiRepository`
- **Purpose**: Handles communication with OpenAI's GPT models.
- **Features**:
  - Supports both standard and streaming responses.
  - Allows custom serialization for JSON schema-based responses.
- **Usage**:
  ```kotlin
  val repository: ChatRepository = OpenAiRepository(apiKey = "your-api-key")
  val responseFlow = repository.chat(
      model = "gpt-4",
      messages = listOf(Message(role = Role.USER, content = Content.Text("What is the capital of France?"))),
      options = CompletionOptions(stream = false, maxTokens = 100)
  )
  ```

### 3. `OllamaRepository`
- **Purpose**: Handles communication with Ollama's AI services.
- **Features**:
  - Supports both standard and streaming responses.
  - Allows custom serialization for schema-based responses.
- **Usage**:
  ```kotlin
  val repository: ChatRepository = OllamaRepository(url = "http://localhost:11434/api/generate")
  val responseFlow = repository.chat(
      model = "ollama-model",
      messages = listOf(Message(role = Role.USER, content = Content.Text("Tell me a joke."))),
      options = CompletionOptions(stream = false, maxTokens = 50)
  )
  ```

## Core Components

### 1. `ChatRepository`
- **Interface**: Defines the contract for all repositories.
- **Methods**:
  - `chat`: Sends messages and receives responses as a flow.
  - `chat<T>`: Sends messages and receives typed responses using a custom serializer.

### 2. `ChatResponse`
- **Purpose**: Represents the response from a chat service.
- **Fields**:
  - `id`: Unique identifier for the response.
  - `response`: List of messages in the response.
  - `tokens`: Token usage details (if available).

### 3. `CompletionOptions`
- **Purpose**: Configures the behavior of the chat request.
- **Fields**:
  - `stream`: Whether to enable streaming responses.
  - `maxTokens`: Maximum number of tokens for the response.

## Testing

Integration tests are provided for each repository:
- `AnthropicRepositoryIntegrationTests`
- `OpenAiRepositoryIntegrationTest`

These tests demonstrate how to use the repositories and validate their behavior.

## Getting Started

1. Add the library to your project.
2. Configure the required API keys and endpoints.
3. Use the repositories to interact with the desired AI service.

## Contributing

Contributions are welcome! Please submit a pull request or open an issue for any bugs or feature requests.
