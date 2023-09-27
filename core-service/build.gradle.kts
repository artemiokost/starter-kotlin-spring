import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "core"
version = "0.0.1"

plugins {
    alias(libs.plugins.flyway) apply true
    alias(libs.plugins.kotlin) apply true
    alias(libs.plugins.kotlin.jpa) apply true
    alias(libs.plugins.kotlin.spring) apply true
    alias(libs.plugins.spring.boot) apply true
}

dependencies {
    annotationProcessor(libs.spring.boot.configuration.processor)

    implementation(libs.bundles.ui)
    implementation(libs.postgresql)
    implementation(platform(libs.spring.cloud.dependencies))
}

repositories {
    mavenCentral()
}

tasks {
    withType<BootJar> {
        archiveFileName.set("ui-service.jar")
    }
    withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "20"
        }
    }
    withType<Wrapper> {
        gradleVersion = libs.versions.gradle.get()
    }
}