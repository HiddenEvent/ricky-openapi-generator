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
    doFirst {
        println("downloadOpenApiSpec .................")
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

// 'Service'로 끝나는 파일을 복사하는 작업
tasks.register<Copy>("moveServiceFiles") {
    println("moveServiceFiles .................")
    // 생성된 파일의 원본 경로
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/org/openapitools/api"

    // 이동할 대상 경로
    val serviceTargetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/service"

    from(sourceDir) // 복사할 파일의 원본 경로
    into(serviceTargetDir) // 복사할 대상 경로
    include("**/*Service.kt") // 'Service'로 끝나는 모든 Kotlin 파일 패턴

    doFirst {
        val files = File(sourceDir).listFiles()
        val serviceCount = files?.count { it.name.endsWith("Service.kt") } ?: 0
        println("Found $serviceCount service files to move.")
    }

    doLast {
        println("Service files moved to the service directory.")
    }
}

// 'Facade'로 끝나는 파일을 복사하는 작업
tasks.register<Copy>("moveFacadeFiles") {
    println("moveFacadeFiles .................")
    // 생성된 파일의 원본 경로
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/org/openapitools/api"

    // 이동할 대상 경로
    val facadeTargetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/facade"

    from(sourceDir) // 복사할 파일의 원본 경로
    into(facadeTargetDir) // 복사할 대상 경로
    include("**/*Facade.kt") // 'Facade'로 끝나는 모든 Kotlin 파일 패턴

    doFirst {
        val files = File(sourceDir).listFiles()
        val facadeCount = files?.count { it.name.endsWith("Facade.kt") } ?: 0
        println("Found $facadeCount facade files to move.")
    }

    doLast {
        println("Facade files moved to the facade directory.")
    }
}

tasks.named("openApiGenerate") {
    dependsOn("downloadOpenApiSpec") // openApiGenerate 작업 전 downloadOpenApiSpec 실행
    finalizedBy("moveServiceFiles") // openApiGenerate 작업 후 moveServiceFiles 실행
    finalizedBy("moveFacadeFiles") // openApiGenerate 작업 후 moveFacadeFiles 실행
}

openApiGenerate {
    println("openApiGenerate .................")
    generatorName.set("kotlin-spring")
//    inputSpec.set(file("npm-codegenerate/openapi-spec/pet-store.json").absolutePath) /* 스팩 직접 파일 지정 시 사용 */
    inputSpec.set(layout.buildDirectory.file("openapi-spec.json").get().asFile.absolutePath)
    outputDir.set(layout.buildDirectory.dir("openapi-kotlin").get().asFile.absolutePath)
//    apiPackage.set("com.example.api")
//    modelPackage.set("com.example.model")
    apiFilesConstrainedTo.set(listOf(""))
    modelFilesConstrainedTo.set(listOf(""))
    supportingFilesConstrainedTo.set(listOf("ApiUtil.java"))
    validateSpec.set(true)
    configOptions.set(
        mapOf(
            "apiSuffix" to "Facade",
            "useTags" to "true",
            "useSpringBoot3" to "true",
            "interfaceOnly" to "true",
            "serviceInterface" to "true",
        )
    )

    // 커스텀 템플릿 디렉토리 설정
    templateDir.set(layout.projectDirectory.dir("src/main/resources/templates").asFile.absolutePath)
}


kotlin {
    sourceSets {
        main {
            kotlin.srcDir(layout.buildDirectory.dir("generated/src/main/kotlin"))
        }
    }
}