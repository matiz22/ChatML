# ChatML

ChatML is a Kotlin Multiplatform Mobile (KMM) library that provides a unified, lightweight layer for interacting with multiple AI chat providers (OpenAI, Anthropic, Ollama). It focuses on sending messages, receiving responses (including streaming), and supporting typed JSON-schema responses.

## Key features

- Unified CompletionRepository interface for multiple providers.
- Streaming responses support for providers that offer server-sent events or line-delimited streams.
- Typed responses via JSON Schema generation and custom serializers.
- Provider-specific repositories: OpenAiCompletionRepository, AnthropicCompletionRepository, OllamaCompletionRepository.
- Kotlin Multiplatform friendly (common code for domain + data layers).

## Adding the library

Add the GitHub Packages (or your preferred Maven repository) and the artifact you need. Example (replace version with the actual latest):

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

Or add the modules directly as source modules if you are consuming the project.

## Supported providers

- OpenAI (OpenAiCompletionRepository)
- Anthropic (AnthropicCompletionRepository)
- Ollama (OllamaCompletionRepository)

## Usage overview

All repositories implement CompletionRepository and expose two primary entry points:

- suspend fun completion(model: String, messages: List<Message>, options: CompletionOptions): Flow<ChatResponse>
- suspend fun <T> completion(model: String, messages: List<Message>, options: CompletionOptions, serializer: KSerializer<T>): Flow<TypedChatResponse<T>>

Examples below show basic usage patterns.

### OpenAI

```kotlin
val repo = OpenAiCompletionRepository(apiKey = "your-openai-key")
val flow = repo.completion(
    model = "gpt-4",
    messages = listOf(Message(role = Role.USER, content = Content.Text("What's the capital of France?"))),
    options = CompletionOptions(stream = false, maxTokens = 100)
)
flow.collect { chatResponse ->
    // handle ChatResponse
}
```

For typed responses (JSON schema -> typed object):

```kotlin
val typedFlow = repo.completion(model, messages, options, MyResponseSerializer)
typedFlow.collect { typedResponse ->
    // typedResponse: TypedChatResponse<MyResponse>
}
```

### Anthropic

```kotlin
val repo = AnthropicCompletionRepository(apiKey = "your-anthropic-key")
val flow = repo.completion(
    model = "claude-3",
    messages = listOf(Message(role = Role.USER, content = Content.Text("Hello!"))),
    options = CompletionOptions(stream = true)
)
flow.collect { chatResponse ->
    // handle streaming ChatResponse updates
}
```

You can also provide a serializer to call anthropic tools via JSON schema and receive typed responses.

### Ollama

```kotlin
val repo = OllamaCompletionRepository(url = "http://localhost:11434/api/generate")
val flow = repo.completion(
    model = "ollama-model",
    messages = listOf(Message(role = Role.USER, content = Content.Text("Tell me a joke."))),
    options = CompletionOptions(stream = false, maxTokens = 50)
)
flow.collect { chatResponse ->
    // handle response
}
```

## Core components

- CompletionRepository (interface)
  - completion(...) : Flow<ChatResponse>
  - completion<T>(..., serializer: KSerializer<T>) : Flow<TypedChatResponse<T>>

- ChatResponse
  - Represents one or more messages returned by a provider; includes metadata such as tokens when available.

- CompletionOptions
  - Configures behavior (streaming, max tokens, etc.).

- Message / Content / Role
  - Domain models representing the chat messages you send and receive.

## Notes

- Streaming behavior differs per provider:
  - OpenAI & Anthropic: SSE-style stream handling.
  - Ollama: line-delimited JSON streaming.
- Typed responses rely on runtime generation of a JSON schema from a Kotlinx serializer descriptor. Use the typed completion method when you expect structured outputs.
- Handle exceptions and stream termination states in collectors (flows can emit partial updates).

## Contributing

Contributions welcome. Open issues or submit pull requests for bug fixes, new provider integrations, or improved serialization/streaming support.

## Testing

Integration tests are included in the repository and demonstrate usage patterns for supported providers (see tests in the data module for examples of streaming and typed responses).
