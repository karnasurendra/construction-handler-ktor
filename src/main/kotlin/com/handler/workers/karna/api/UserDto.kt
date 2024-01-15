package com.handler.workers.karna.api

import com.handler.workers.karna.domain.ConstructionExpertise
import kotlinx.serialization.Serializable


@Serializable
data class UserDto(
    val userName: String,
    val mobileNumber: String,
    val pin: String,
    val experience: Int,
    val expertiseIn: List<ConstructionExpertise>
)

@Serializable
data class UserOverviewDto(
    val userName: String,
    val mobileNumber: String,
    val createdOn: Long,
    val experience: Int,
    val expertiseIn: List<ConstructionExpertise>
)

@Serializable
data class UserLoginDto(
    val mobileNumber: String,
    val pin: String
)