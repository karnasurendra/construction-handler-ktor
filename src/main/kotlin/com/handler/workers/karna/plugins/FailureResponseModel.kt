package com.handler.workers.karna.plugins

import kotlinx.serialization.Serializable

@Serializable
data class FailureResponseModel(
    val statusCode: Int,
    val message: String
)
