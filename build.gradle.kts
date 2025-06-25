plugins {
    kotlin("jvm") version "2.2.0"
    `maven-publish`
}

group = "com.github.encryptsl.discord"
version = "1.0.3"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    compileOnly("net.dv8tion:JDA:5.6.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}

publishing {
    repositories {
        mavenLocal()
    }
    publications.create<MavenPublication>("libs").from(components["kotlin"])
}