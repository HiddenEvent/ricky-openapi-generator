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

apply(from = "gradle/openapi.gradle.kts")

openApiGenerate {
    generatorName.set("kotlin")
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
    apiFilesConstrainedTo.set(listOf(""))
    modelFilesConstrainedTo.set(listOf("User","Board","BoardComment"))
    supportingFilesConstrainedTo.set(listOf("ApiUtil.java"))
    apiPackage.set("me.ricky.rest")
    modelPackage.set("me.ricky.storage")
    validateSpec.set(true)
    configOptions.set(
        mapOf(
            "apiSuffix" to "",
            "sortModelPropertiesByRequiredFlag" to "true",
            "sortParamsByRequiredFlag" to "true",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
            "serviceInterface" to "true"
        )
    )
    additionalProperties.set(
        mapOf(
            "ignoredFields" to "id,creatorId"
        )
    )
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}