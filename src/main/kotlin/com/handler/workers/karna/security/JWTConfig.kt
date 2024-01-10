package com.handler.workers.karna.security

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.handler.workers.karna.configuration.ApplicationConfiguration
import java.util.*

object JWTConfig {

    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val algorithm = Algorithm.HMAC512(ApplicationConfiguration.jwtConfig.jwtSecret)

    val verifier: JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(ApplicationConfiguration.jwtConfig.issuer)
        .build()

    /**
     * Produce a token for this combination of User and Account
     */
    fun makeToken(userId: String): String {
        return JWT.create()
            .withSubject(ApplicationConfiguration.jwtConfig.jwtSecret)
            .withIssuer(ApplicationConfiguration.jwtConfig.issuer)
            .withClaim("id", userId)
            .withExpiresAt(getExpiration())
            .sign(algorithm)
    }

    /**
     * Calculate the expiration Date based on current time + the given validity
     */
    private fun getExpiration() = Date(System.currentTimeMillis() + validityInMs)

}