package com.handler.workers.karna.plugins

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject

@Serializable
data class SuccessResponseModel(
    val statusCode: Int,
    val message: String,
    val data: JsonObject? = null
)

@Serializable
data class SuccessResponseModelTwo(
    val statusCode: Int,
    val message: String,
    val data: JsonArray? = null
)

@Serializable
data class TokenResponse(val token: String)


