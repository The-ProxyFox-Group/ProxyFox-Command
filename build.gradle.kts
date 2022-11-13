import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "dev.proxyfox"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    explicitApi()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}