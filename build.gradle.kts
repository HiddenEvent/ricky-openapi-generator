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

val basePackageName by extra("me.ricky")
val apiPackageName by extra("${basePackageName}.api")
val modelPackageName by extra("${basePackageName}.storage")

openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
//    apiFilesConstrainedTo.set(listOf("Board"))
//    modelFilesConstrainedTo.set(listOf("User", "Board", "BoardComment"))
//    supportingFilesConstrainedTo.set(listOf("ApiUtil.java"))
    validateSpec.set(true)
    configOptions.set(
        mapOf(
            "apiSuffix" to "",
            "basePackage" to basePackageName,
            "apiPackage" to apiPackageName,
            "modelPackage" to modelPackageName,
            "useSpringBoot3" to "true",
            "useTags" to "true",
//            "sortModelPropertiesByRequiredFlag" to "true",
//            "sortParamsByRequiredFlag" to "true",
            "serviceImplementation" to "true",
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