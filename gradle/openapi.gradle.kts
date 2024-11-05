import org.gradle.api.tasks.Copy
import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

val apiPackagePath by extra("me/ricky/api")
val modelPackagePath by extra("me/ricky/storage")

tasks.register("downloadOpenApiSpec") {
    doFirst {
        println("download OpenApiSpec .................")
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

tasks.register<Copy>("moveServiceImpleFiles") {
    println("convert Service.................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/aggregate/service"

    from(sourceDir)
    into(targetDir)
    exclude("**/*PublicServiceImpl.kt")
    exclude("**/*AdminServiceImpl.kt")
    include("**/*ServiceImpl.kt")

    eachFile {
        this.name = this.name.replace("ServiceImpl.kt", "Service.kt")
        println(this.name)
    }
    // 항상 작업이 실행되도록 설정
    outputs.upToDateWhen { false }
}

tasks.register<Copy>("moveServiceFiles") {
    println("convert Store.................")
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
    println("convert JpaStore.................")
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
    // 항상 작업이 실행되도록 설정
    outputs.upToDateWhen { false }
}

tasks.register<Copy>("moveEntityFiles") {
    println("convert Entity.................")
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
    println("convert Test.................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/test/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/test"

    from(sourceDir)
    into(targetDir)

    eachFile {
        this.name = this.name.replace("Test.kt", "IT.kt")
        println(this.name)
    }
    outputs.upToDateWhen { false }
}

tasks.register<Copy>("moveDelegateFiles") {
    println("convert Repository.................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val targetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/storage/store/repository"

    from(sourceDir)
    into(targetDir)
    exclude("**/*PublicDelegate.kt")
    exclude("**/*AdminDelegate.kt")
    include("**/*Delegate.kt")

    eachFile {
        this.name = this.name.replace("Delegate.kt", "Repository.kt")
        println(this.name)
    }
}
tasks.register<Delete>("testDirDelete") {
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/test/kotlin/${apiPackagePath}"

    delete(sourceDir)
    doFirst {
        println("Deleting directory: $sourceDir")
    }
}

tasks.named("openApiGenerate") {
    dependsOn("testDirDelete")
    finalizedBy(
        "moveServiceImpleFiles",
        "moveServiceFiles",
        "moveControllerFiles",
        "moveEntityFiles",
        "moveTestFiles",
        "moveDelegateFiles"
    )
}

