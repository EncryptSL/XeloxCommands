plugins {
    kotlin("jvm") version "1.9.0" apply true
    `maven-publish`
}

group = "com.github.encryptsl.discord"
version = "1.0.2"

repositories {
    mavenLocal()
    mavenCentral()
}

dependencies {
    compileOnly("net.dv8tion:JDA:5.0.0-beta.13")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}

publishing {
    repositories {
        mavenLocal()
    }
    publications.create<MavenPublication>("libs").from(components["kotlin"])
}