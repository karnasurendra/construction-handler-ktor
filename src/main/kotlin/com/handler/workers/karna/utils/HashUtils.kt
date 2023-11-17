package com.handler.workers.karna.utils

import java.security.MessageDigest

object HashUtils {

    fun sha256(input: String): String {
        val bytes = input.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }

    fun verifyHash(hashedPIN: String, valueToHash: String): Boolean {
        return sha256(valueToHash) == hashedPIN
    }

}