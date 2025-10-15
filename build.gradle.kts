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

// 두번째
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
//    apiFilesConstrainedTo.set(listOf("Board"))
//    modelFilesConstrainedTo.set(listOf("User", "Board", "BoardComment"))
//    supportingFilesConstrainedTo.set(listOf("ApiUtil.java"))
    validateSpec.set(true)
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
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
}


// sample 추가를 위해
tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateSample") {
    generatorName.set("kotlin-spring")
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
    additionalProperties.set(
        mapOf(
            "isSample" to "true"
        )
    )
    configOptions.set(
        mapOf(
            "basePackage" to basePackageName,
            "apiPackage" to apiPackageName,
            "modelPackage" to modelPackageName,
            "useSpringBoot3" to "true",
            "useTags" to "true",
            "delegatePattern" to "true",
        )
    )
}

tasks.register<Copy>("moveSampleFiles") {
    dependsOn("openApiGenerateSample")
    doFirst {
        println("convert Sample.................")  // ✅ 실행 단계에서 출력
    }
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/me/ricky/storage"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/spec/sample"

    from(sourceDir)
    into(targetDir)
    exclude("JwtToken.kt")
    exclude("**/*Qdo.kt")
    exclude("**/OffsetElements*")
    exclude("**/ExceptionBody.kt")

    eachFile {
        this.name = this.name.replace(".kt", "Sample.kt")
        println(this.name)
    }
}

// fixture 추가를 위해
tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerateFixture") {
    dependsOn("moveSampleFiles")
    generatorName.set("kotlin-spring")
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
    additionalProperties.set(
        mapOf(
            "isFixture" to "true"
        )
    )
    configOptions.set(
        mapOf(
            "basePackage" to basePackageName,
            "apiPackage" to apiPackageName,
            "modelPackage" to modelPackageName,
            "useSpringBoot3" to "true",
            "useTags" to "true",
            "delegatePattern" to "true",
        )
    )
}

tasks.register<Copy>("moveFixtureFiles") {
    dependsOn("openApiGenerateFixture")
    doFirst {
        println("convert Fixture.................")  // ✅ 실행 단계에서 출력
    }
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/me/ricky/storage"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/test/fixture"

    from(sourceDir)
    into(targetDir)
    exclude("JwtToken.kt")
    exclude("**/*Qdo.kt")
    exclude("**/OffsetElements*")
    exclude("**/ExceptionBody.kt")

    eachFile {
        this.name = this.name.replace(".kt", "Fixture.kt")
        println(this.name)
    }
}

// build.gradle.kts
tasks.register("generateOpenAPI") {
    dependsOn("downloadOpenApiSpec")
    dependsOn( "openApiGenerateSample", "moveSampleFiles")
    dependsOn("openApiGenerateFixture", "moveFixtureFiles")
    finalizedBy("openApiGenerate")
}

kotlin {
    sourceSets {
        main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}