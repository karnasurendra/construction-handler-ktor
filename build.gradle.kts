val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val mongodb_version: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "com.handler.workers.karna"
version = "0.0.1"

application {
    mainClass.set("com.handler.workers.karna.MainKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("org.litote.kmongo:kmongo:$mongodb_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    /*Json*/
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    /*Authentication*/
    implementation("io.ktor:ktor-server-auth")
    implementation("io.ktor:ktor-server-auth-jwt")
    implementation("io.ktor:ktor-http")
    implementation("io.ktor:ktor-network-tls:2.1.3")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
}

tasks.register("databaseInstance"){
    doLast {
        val command = arrayOf("docker-compose", "up")
        Runtime.getRuntime().exec(command)
    }
}
