import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.21"
    `maven-publish`
}

group = "dev.proxyfox"
version = "1.5"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
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
