group = "starter.common"
version = "0.0.1"

plugins {
    alias(libs.plugins.kotlin) apply true
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.common)
    implementation(libs.bundles.kotlin)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
    jvmToolchain(21)
}

tasks {
    withType<Wrapper> {
        gradleVersion = libs.versions.gradle.get()
    }
}