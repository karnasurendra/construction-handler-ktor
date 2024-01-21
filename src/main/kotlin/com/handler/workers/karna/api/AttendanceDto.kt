package com.handler.workers.karna.api

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceDto(
    val workerId: String = String(),
    val attendedDate: Long
)