package com.handler.workers.karna.utils

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    val statusCode: Int,
    val message: String,
    var data: T? = null
)
