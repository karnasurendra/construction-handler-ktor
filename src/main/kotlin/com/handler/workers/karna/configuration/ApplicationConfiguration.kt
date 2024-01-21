package com.handler.workers.karna.configuration

import io.ktor.server.application.*


object ApplicationConfiguration {
    lateinit var securityConfig: SecurityConfig
    lateinit var jwtConfig: JwtConfig
}

fun Application.setupApplicationConfiguration() {

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
