package com.handler.workers.karna.configuration

import io.ktor.server.application.*


object ApplicationConfiguration {
    lateinit var securityConfig: SecurityConfig
    lateinit var ssl: Ssl
    lateinit var jwtConfig: JwtConfig
    var isDevelopment: Boolean = true
}

fun Application.setupApplicationConfiguration() {

    ApplicationConfiguration.isDevelopment =
        environment.config.propertyOrNull("ktor.development")?.getString()?.toBoolean() ?: false


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

    //SSL
    val ssl = environment.config.config("ktor.ssl")
    val port = ssl.property("port").getString().toInt()
    val keyStorePath = ssl.property("keyStorePath").getString()
    val keyAlias = ssl.property("keyAlias").getString()
    val keyStorePassword = ssl.property("keyStorePassword").getString()
    val privateKeyPassword = ssl.property("privateKeyPassword").getString()
    ApplicationConfiguration.ssl = Ssl(port, keyStorePath, keyAlias, keyStorePassword, privateKeyPassword)
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

data class Ssl(
    val port: Int,
    val keystorePath: String,
    val keyAlias: String,
    val keyStorePassword: String,
    val privateKeyPassword: String
)
