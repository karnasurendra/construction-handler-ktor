val ktorVersion: String by project
val kotlinVersion: String by project
val exposedVersion: String by project
val logbackVersion: String by project
val mongodbVersion: String by project
val koinVersion: String by project

plugins {
    kotlin("jvm") version "1.9.20"
    id("io.ktor.plugin") version "2.3.6"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
}

group = "com.handler.workers.karna"
version = "0.0.1"

application {
    mainClass.set("com.handler.workers.karna.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
    implementation("org.litote.kmongo:kmongo-coroutine:4.11.0")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")

    /*Authentication*/
    implementation("io.ktor:ktor-http")
    implementation("io.ktor:ktor-network-tls:2.1.3")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:$ktorVersion")

    // koin
    implementation("io.insert-koin:koin-ktor:$koinVersion")
    implementation("io.insert-koin:koin-logger-slf4j:$koinVersion")
    implementation("io.ktor:ktor-server-core-jvm:2.3.6")
    implementation("io.ktor:ktor-server-host-common-jvm:2.3.6")
    implementation("io.ktor:ktor-server-call-logging-jvm:2.3.6")
    implementation("io.ktor:ktor-server-auth-jvm:2.3.6")

    // Testing
    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")

    // exposed
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")


}

tasks.register("startDatabase"){
    doLast {
        val command = arrayOf("docker-compose", "up")
        Runtime.getRuntime().exec(command)
    }
}

tasks.register("runKtorWithDatabase") {
    dependsOn("startDatabase")
    doLast {
        val runCommand = arrayOf("./gradlew", "run")
        val process = Runtime.getRuntime().exec(runCommand)
        process.waitFor()
    }
}