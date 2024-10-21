import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.gradle.api.tasks.Copy
import java.io.File

val apiPackagePath by extra("me/ricky/api")
val modelPackagePath by extra("me/ricky/storage")

tasks.register("downloadOpenApiSpec") {
    doFirst {
        println("downloadOpenApiSpec .................")
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

tasks.register<Copy>("moveServiceImpleFiles") {
    println("convertService .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/aggregate/service"

    from(sourceDir)
    into(targetDir)
    include("**/*ServiceImpl.kt")

    eachFile {
        this.name = this.name.replace("ServiceImpl.kt", "Service.kt")
        println(this.name)
    }
}

tasks.register<Copy>("moveServiceFiles") {
    println("convertStoreFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/aggregate/store"

    from(sourceDir)
    into(targetDir)
    exclude("**/*PublicService.kt")
    exclude("**/*AdminService.kt")
    include("**/*Service.kt")

    eachFile {
        this.name = this.name.replace("Service.kt", "Store.kt")
        println(this.name)
    }
}

tasks.register<Copy>("moveControllerFiles") {
    println("moveJpaStoreFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/storage/store"

    from(sourceDir)
    into(targetDir)
    exclude("**/*PublicController.kt")
    exclude("**/*AdminController.kt")
    include("**/*Controller.kt")

    eachFile {
        this.name = this.name.replace("Controller.kt", "JpaStore.kt")
        println(this.name)
    }
}

tasks.register<Copy>("moveEntityFiles") {
    println("moveEntityFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${modelPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/storage/entity"

    from(sourceDir)
    into(targetDir)
    exclude("JwtToken.kt")
    exclude("**/*Qdo.kt")
    exclude("**/OffsetElements*")
    exclude("**/ExceptionBody.kt")

    eachFile {
        this.name = this.name.replace(".kt", "Entity.kt")
        println(this.name)
    }
}

tasks.register<Copy>("moveTestFiles") {
    println("통합 테스트 파일 변경 .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/test/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/test"

    from(sourceDir)
    into(targetDir)

    eachFile {
        this.name = this.name.replace("Test.kt", "IT.kt")
        println(this.name)
    }
}

tasks.named("openApiGenerate") {
    dependsOn("downloadOpenApiSpec")
    finalizedBy("moveServiceImpleFiles", "moveServiceFiles", "moveControllerFiles", "moveEntityFiles", "moveTestFiles")
}