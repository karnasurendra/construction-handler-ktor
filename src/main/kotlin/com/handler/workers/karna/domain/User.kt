package com.handler.workers.karna.domain

import com.handler.workers.karna.security.PasswordEncryptionResult
import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

data class User(
    @BsonId
    val id: String? = null,
    val username: String,
    val mobileNumber: String,
    val pin: PasswordEncryptionResult,
    val securitySettings: SecuritySettings,
    val created: LocalDateTime = LocalDateTime.now().truncatedTo(ChronoUnit.MILLIS)
)
