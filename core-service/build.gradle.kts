import org.springframework.boot.gradle.tasks.bundling.BootJar

group = "starter.core"
version = "0.0.1"

plugins {
    alias(libs.plugins.flyway) apply true
    alias(libs.plugins.kotlin) apply true
    alias(libs.plugins.kotlin.spring) apply true
    alias(libs.plugins.spring.boot) apply true
}

repositories {
    mavenCentral()
}

dependencies {
    annotationProcessor(libs.spring.boot.configuration.processor)

    implementation(libs.bundles.core)
    implementation(libs.bundles.kotlin)
    implementation(libs.netty.resolver.dns.native.macos) {
        artifact { classifier = "osx-aarch_64" }
    }
    implementation(platform(libs.spring.cloud.dependencies))
    implementation(project(":common"))
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
    jvmToolchain(23)
}

tasks {
    withType<BootJar> {
        archiveFileName.set("core-service.jar")
    }
    withType<Wrapper> {
        gradleVersion = libs.versions.gradle.get()
    }
}