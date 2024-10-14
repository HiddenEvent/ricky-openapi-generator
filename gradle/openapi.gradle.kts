import java.net.URI
import kotlin.io.path.createDirectories
import kotlin.io.path.writeText
import org.gradle.api.tasks.Copy
import java.io.File

val apiPackageName by extra("com.example.api")
val apiPackagePath by extra("com/example/api")
val modelPackageName by extra("com.example.model")
val modelPackagePath by extra("com/example/model")

tasks.register("downloadOpenApiSpec") {
    doFirst {
        println("downloadOpenApiSpec .................")
        val specUri = URI("http://localhost:9200/v3/api-docs/1-all").toURL()
        val specPath = layout.buildDirectory.file("openapi-spec.json").get().asFile.toPath()
        specPath.parent.createDirectories()
        specPath.writeText(specUri.readText())
    }
}

tasks.register<Copy>("moveServiceFiles") {
    println("moveServiceFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val serviceTargetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/service"

    from(sourceDir)
    into(serviceTargetDir)
    include("**/*Service.kt")

    doFirst {
        val files = File(sourceDir).listFiles()
        val serviceCount = files?.count { it.name.endsWith("Service.kt") } ?: 0
        println("Found $serviceCount service files to move.")
    }
}

tasks.register<Copy>("moveFacadeFiles") {
    println("moveFacadeFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${apiPackagePath}"
    val facadeTargetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/facade"

    from(sourceDir)
    into(facadeTargetDir)
    exclude("**/*Service.kt")

    eachFile {
        this.name = this.name.replace(".kt", "Facade.kt")
        println(this.name)
    }

    doFirst {
        val files = File(sourceDir).listFiles()
        val facadeCount = files?.count { it.name.endsWith("Facade.kt") } ?: 0
        println("Found $facadeCount facade files to move.")
    }
}

tasks.register<Copy>("moveEntityFiles") {
    println("moveEntityFiles .................")
    val sourceDir = "${layout.buildDirectory.get().asFile}/openapi-kotlin/src/main/kotlin/${modelPackagePath}"
    val entityTargetDir = "${layout.buildDirectory.get().asFile}/ricky-generator/entity"

    from(sourceDir)
    into(entityTargetDir)

    eachFile {
        this.name = this.name.replace(".kt", "Entity.kt")
        println(this.name)
    }
}

tasks.named("openApiGenerate") {
    dependsOn("downloadOpenApiSpec")
    finalizedBy("moveServiceFiles", "moveFacadeFiles", "moveEntityFiles")
}