import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

plugins {
    kotlin("jvm") version "2.0.20"
    id("org.openapi.generator") version "7.8.0"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

tasks.register("downloadOpenApiSpec") {
    doLast {
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

openApiGenerate {
    generatorName.set("kotlin-spring")
//    inputSpec.set(file("npm-codegenerate/openapi-spec/pet-store.json").absolutePath) /* 스팩 직접 파일 지정 시 사용 */
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)

//    apiNameSuffix.set("Facade") /*[kotlin-spring] 생성 타입의 경우 사용 사용불가*/
    apiPackage.set("com.example.api")
    modelPackage.set("com.example.model")

    apiFilesConstrainedTo.set(listOf(""))
    modelFilesConstrainedTo.set(listOf(""))
    supportingFilesConstrainedTo.set(listOf("ApiUtil.java"))
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "serviceInterface" to "true",
//        "delegatePattern" to "true",
        )
    )
    validateSpec.set(true)

    // 커스텀 템플릿 디렉토리 설정
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
}

tasks.named("openApiGenerate") {
    dependsOn("downloadOpenApiSpec")
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}