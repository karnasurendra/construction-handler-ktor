package com.handler.workers.karna.security

import com.handler.workers.karna.configuration.ApplicationConfiguration
import com.handler.workers.karna.domain.SecuritySettings
import kotlinx.serialization.Serializable
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

@Serializable
data class PasswordEncryptionResult(
    val initializationVector: ByteArray,
    val nonce: ByteArray,
    val hashSum: ByteArray,
    val encryptedPassword: ByteArray,
    val salt: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PasswordEncryptionResult

        if (!initializationVector.contentEquals(other.initializationVector)) return false
        if (!nonce.contentEquals(other.nonce)) return false
        if (!hashSum.contentEquals(other.hashSum)) return false
        if (!encryptedPassword.contentEquals(other.encryptedPassword)) return false
        if (!salt.contentEquals(other.salt)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = initializationVector.contentHashCode()
        result = 31 * result + nonce.contentHashCode()
        result = 31 * result + hashSum.contentHashCode()
        result = 31 * result + salt.contentHashCode()
        return result
    }
}

object EncryptionManager {

    private val logger: Logger = LoggerFactory.getLogger(EncryptionManager::class.java)

    private fun generateSalt(): ByteArray {
        val salt = ByteArray(ApplicationConfiguration.securityConfig.defaultSaltLengthBytes)
        val random: SecureRandom = SecureRandom.getInstanceStrong()
        random.nextBytes(salt)
        return salt
    }

    private fun generateSecretKey(
        password: String,
        salt: ByteArray,
        iterationCount: Int,
        passwordKeySize: Int
    ): SecretKey {
        val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512")
        val passwordBasedEncryptionKeySpec: KeySpec = PBEKeySpec(
            password.toCharArray(), salt, iterationCount,
            passwordKeySize
        )
        val secretKeyFromPBKDF2 = secretKeyFactory.generateSecret(passwordBasedEncryptionKeySpec)
        return SecretKeySpec(secretKeyFromPBKDF2.encoded, "AES")
    }

    private fun createNonce(): ByteArray {
        val random = SecureRandom.getInstanceStrong()
        val nonce = ByteArray(ApplicationConfiguration.securityConfig.defaultNonceLengthBytes)
        random.nextBytes(nonce)
        return nonce
    }

    private fun setupCipher(nonce: ByteArray): Pair<Cipher, GCMParameterSpec> {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(ApplicationConfiguration.securityConfig.defaultGcmParameterSpecLength, nonce)
        return Pair(cipher, spec)
    }

    private fun getMessageDigest(): MessageDigest =
        MessageDigest.getInstance(ApplicationConfiguration.securityConfig.fileIntegrityCheckHashingAlgorithm)

    fun encryptString(password: String): PasswordEncryptionResult {
        try {
            val salt = generateSalt()

            val key: SecretKey = generateSecretKey(
                password = password,
                salt = salt,
                iterationCount = ApplicationConfiguration.securityConfig.defaultIterationCount,
                passwordKeySize = ApplicationConfiguration.securityConfig.defaultPasswordKeySizeBytes
            )

            val nonce = createNonce()

            val (cipher, spec) = setupCipher(nonce)
            cipher.init(Cipher.ENCRYPT_MODE, key, spec)

            val encryptedPassword = cipher.doFinal(password.encodeToByteArray())

            val messageDigest = getMessageDigest()
            messageDigest.update(encryptedPassword)
            return PasswordEncryptionResult(
                initializationVector = cipher.iv,
                hashSum = messageDigest.digest(),
                nonce = nonce,
                encryptedPassword = encryptedPassword,
                salt = salt
            )
        } catch (e: Exception) {
            logger.error("Failed to encrypt password.", e)
            throw EncryptionException("Failed to encrypt password.")
        }
    }

    @SuppressWarnings("TooGenericExceptionCaught") // It's intended to catch all exceptions in this layer
    fun decryptString(
        encryptionResult: PasswordEncryptionResult,
        settings: SecuritySettings,
        password: String
    ): String {
        try {
            val key: SecretKey = generateSecretKey(
                password = password,
                salt = encryptionResult.salt,
                iterationCount = settings.iterationCount,
                passwordKeySize = settings.passwordKeySizeBytes
            )

            val messageDigest = getMessageDigest()
            messageDigest.update(encryptionResult.encryptedPassword)
            val digest = messageDigest.digest()
            if (!digest.contentEquals(encryptionResult.hashSum)) {
                throw IntegrityFailedException(
                    "Integrity check failed (expected: " +
                            "${encryptionResult.hashSum.contentToString()}, got: ${digest.contentToString()}."
                )
            }

            val (cipher, spec) = setupCipher(encryptionResult.nonce)
            cipher.init(Cipher.DECRYPT_MODE, key, spec)
            val decryptedPassword = cipher.doFinal(encryptionResult.encryptedPassword)
            return decryptedPassword.decodeToString()
        } catch (e: Exception) {
            logger.error("Failed to decrypt string.", e)
            throw EncryptionException("Failed to decrypt string.")
        }
    }


}