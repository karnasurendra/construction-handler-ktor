package com.handler.workers.karna.domain

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime

data class Attendance(
    @BsonId
    val id: String = String(),
    val workerId: String = String(),
    val attendedDate: LocalDateTime
)