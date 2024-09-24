object UserDefineInputs {
    data class GenerateTaskInput(val name: String, val specUrl: String)

    val provideSpec = GenerateTaskInput("my-component", "http://localhost:9200/v3/api-docs/1.%EC%A0%84%EC%B2%B4")

    val consumeSpecs = listOf(
        GenerateTaskInput("componentA", "https://api.swaggerhub.com/apis/test/component-a/1.0.0"),
        GenerateTaskInput("componentB", "https://api.swaggerhub.com/apis/test/component-b/1.0.0")
    )
}