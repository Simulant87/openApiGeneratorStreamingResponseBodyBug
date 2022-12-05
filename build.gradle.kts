import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	val springBootVersion = "2.7.6"
	val kotlinVersion = "1.7.22"

	java
	id("org.springframework.boot").version(springBootVersion)
	id("io.spring.dependency-management") version "1.1.0"
	// cannot update to 6.0.1 or higher as it does no longer support the StreamingResponseBody response
	// this works with version 6.0.0
	id("org.openapi.generator") version "6.0.1"
	kotlin("jvm").version(kotlinVersion)
	kotlin("plugin.spring").version(kotlinVersion)
}

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(17))
	}
}



apply(plugin = "io.spring.dependency-management")

group = "com.example.filestorage"

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")

	implementation("org.openapitools:jackson-databind-nullable:0.2.4")
	implementation("commons-io:commons-io:2.11.0")

	// Swagger API documentation
	implementation("org.springdoc:springdoc-openapi-ui:1.6.13")

}

sourceSets {
	getByName("main").java.srcDirs("src/main/kotlin", "generated/src/main/java")
}

tasks {
	openApiGenerate {
		generatorName.value("spring")
		inputSpec.value("$rootDir/api-doc/file-storage-api.yml")
		outputDir.value("$rootDir/generated")

		apiPackage.value("com.example.filestorage.generated.api")
		modelPackage.value("com.example.filestorage.generated.model")

		modelFilesConstrainedTo.value(ArrayList())
		typeMappings.put("object", "com.fasterxml.jackson.databind.JsonNode")
		configOptions.put("dateLibrary", "java8")
		configOptions.put("interfaceOnly", "true")
		importMappings.put("StreamingResponseBody", "org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody")
	}


	val cleanupGenerated by registering (Delete::class) {
		delete = File("generated")
			.walkTopDown()
			.filter { it.name != ".openapi-generator-ignore" && it.name != "generated" }
			.toSet()
	}

	clean {
		dependsOn(cleanupGenerated)
	}

	compileKotlin {
		dependsOn(openApiGenerate)
	}

	compileJava {
		dependsOn(openApiGenerate)
	}

}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}