import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.0"
    kotlin("plugin.serialization") version "1.9.0"
    `maven-publish`
}

group = "dev.proxyfox"
version = "2.0"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.5.1")
    implementation("io.arrow-kt:arrow-core:1.2.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
}

publishing {
    publications {
        register<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        maven {
            url = uri("https://maven.proxyfox.dev")
            credentials.username = System.getenv("PF_MAVEN_USER")
            credentials.password = System.getenv("PF_MAVEN_PASS")
        }
    }
}
