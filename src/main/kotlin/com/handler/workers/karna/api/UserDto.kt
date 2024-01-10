package com.handler.workers.karna.api

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime


@Serializable
data class UserDto(
    val userName: String,
    val mobileNumber: String,
    val pin: String
)

@Serializable
data class UserOverviewDto(
    val userName: String,
    val mobileNumber: String,
    val createdOn: Long
)

@Serializable
data class UserLoginDto(
    val mobileNumber: String,
    val pin: String
)