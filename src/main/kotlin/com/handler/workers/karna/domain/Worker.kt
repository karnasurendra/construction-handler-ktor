package com.handler.workers.karna.domain

import org.bson.codecs.pojo.annotations.BsonId
import java.time.LocalDateTime

data class Worker(
    @BsonId
    val id: String? = null,
    val name: String,
    val experienceInYears: Int,
    val mobileNumber: String,
    val dailyWage: Int,
    val advance: Int,
    val expertiseIn: List<ConstructionExpertise>,
    val userId: String,
    val joinedDate: LocalDateTime,
    val advanceTookDate: LocalDateTime?,
    val status: WorkerStatus
)

enum class WorkerStatus {
    ACTIVE, INACTIVE
}
