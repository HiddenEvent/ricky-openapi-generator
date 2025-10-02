import org.gradle.api.tasks.Copy
import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText

val apiPackagePath by extra("me/ricky/api")
val modelPackagePath by extra("me/ricky/storage")

/**
 * 로컬 서버에서 OpenAPI 명세를 다운로드하여 JSON 파일로 저장하는 태스크
 */
tasks.register("downloadOpenApiSpec") {
    doFirst {
        println("download OpenApiSpec .................")
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

/**
 * ServiceImpl 파일을 Service 파일로 복사 및 이름 변경하는 태스크 (Public, Admin 구현체 제외)
 */
tasks.register<Copy>("moveServiceFiles") {
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

/**
 * Service 파일을 Store 파일로 복사 및 이름 변경하는 태스크 (Public, Admin 서비스 제외)
 */
tasks.register<Copy>("moveStoreFiles") {
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

/**
 * Controller 파일을 JpaStore 파일로 복사 및 이름 변경하는 태스크 (Public, Admin 컨트롤러 제외)
 */
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

/**
 * 모델 파일을 Entity 파일로 복사 및 이름 변경하는 태스크 (JwtToken, Qdo 등 특정 파일 제외)
 */
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

/**
 * Test 파일을 IT (통합 테스트) 파일로 복사 및 이름 변경하는 태스크
 */
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

/**
 * Delegate 파일을 Repository 파일로 복사 및 이름 변경하는 태스크 (Public, Admin delegate 제외)
 */
tasks.register<Copy>("moveRepositoryFiles") {
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

/**
 * 새로운 OpenAPI 파일 생성 전 테스트 디렉토리를 삭제하는 태스크
 */
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
        "moveServiceFiles",
        "moveStoreFiles",
        "moveControllerFiles",
        "moveEntityFiles",
        "moveTestFiles",
        "moveRepositoryFiles"
    )
}

