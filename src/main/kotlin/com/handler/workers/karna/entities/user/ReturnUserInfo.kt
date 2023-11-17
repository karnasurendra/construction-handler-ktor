package com.handler.workers.karna.entities.user

import kotlinx.serialization.Serializable
import org.litote.kmongo.Id

@Serializable
data class ReturnUserInfo(
    val phoneNumber: Long,
    val name: String
)
