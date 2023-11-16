package com.handler.workers.karna.entities.user

import kotlinx.serialization.Serializable

@Serializable
data class CreateUser(
    val id: String? = null,
    val phoneNumber: Long,
    val mPin: String,
    val name: String = String()
)
