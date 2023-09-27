import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "common"
version = "0.0.1"

plugins {
    alias(libs.plugins.kotlin) apply true
}

dependencies {
    implementation(libs.bundles.kotlin)
    implementation(libs.bundles.logger)
}

repositories {
    mavenCentral()
}

tasks {
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