package com.handler.workers.karna.configuration

import io.ktor.server.application.*


object ApplicationConfiguration {
    lateinit var databaseConfig: DatabaseConfig
    lateinit var securityConfig: SecurityConfig
    lateinit var jwtConfig: JwtConfig
}

fun Application.setupApplicationConfiguration() {

    val databaseObject = environment.config.config("ktor.database")
    val url = databaseObject.property("url").getString()
    val user = databaseObject.property("user").getString()
    val password = databaseObject.property("password").getString()
    ApplicationConfiguration.databaseConfig = DatabaseConfig(
        url = url,
        user = user,
        password = password
    )

    // Security
    val securityObject = environment.config.config("ktor.security")
    val fileIntegrityCheckHashingAlgorithm =
        securityObject.property("fileIntegrityCheckHashingAlgorithm").getString()
    val defaultPasswordKeySize = securityObject.property("defaultPasswordKeySizeBytes").getString().toInt()
    val defaultNonceLength = securityObject.property("defaultNonceLengthBytes").getString().toInt()
    val defaultSaltLength = securityObject.property("defaultSaltLengthBytes").getString().toInt()
    val defaultIterationCount = securityObject.property("defaultIterationCount").getString().toInt()
    val defaultGcmParameterSpecLength = securityObject.property("defaultGcmParameterSpecLength").getString().toInt()
    ApplicationConfiguration.securityConfig = SecurityConfig(
        fileIntegrityCheckHashingAlgorithm = fileIntegrityCheckHashingAlgorithm,
        defaultPasswordKeySizeBytes = defaultPasswordKeySize,
        defaultNonceLengthBytes = defaultNonceLength,
        defaultSaltLengthBytes = defaultSaltLength,
        defaultIterationCount = defaultIterationCount,
        defaultGcmParameterSpecLength = defaultGcmParameterSpecLength
    )

    println(ApplicationConfiguration.databaseConfig)

    // Jwt
    val jwtObject = environment.config.config("ktor.jwt")
    val jwtSecret = jwtObject.property("jwtSecret").getString()
    val issuer = jwtObject.property("issuer").getString()
    ApplicationConfiguration.jwtConfig = JwtConfig(jwtSecret, issuer)
}

data class SecurityConfig(
    val fileIntegrityCheckHashingAlgorithm: String,
    val defaultPasswordKeySizeBytes: Int,
    val defaultNonceLengthBytes: Int,
    val defaultSaltLengthBytes: Int,
    val defaultIterationCount: Int,
    val defaultGcmParameterSpecLength: Int
)

data class JwtConfig(
    val jwtSecret: String,
    val issuer: String
)

data class DatabaseConfig(
    val url: String,
    val user: String,
    val password: String
)