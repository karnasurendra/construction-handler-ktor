package com.handler.workers.karna.api

import com.handler.workers.karna.domain.ConstructionExpertise
import com.handler.workers.karna.domain.Worker
import com.handler.workers.karna.domain.WorkerStatus
import com.handler.workers.karna.utils.CommonUtils
import kotlinx.serialization.Serializable

@Serializable
data class WorkerDto(
    val name: String,
    val experienceInYears: Int,
    val mobileNumber: String,
    val dailyWage: Int,
    val advanceAmount: Int? = 0,
    val expertiseIn: List<ConstructionExpertise>,
    val joinedDate: Long,
    val advanceTookDate: Long?,
    val status: WorkerStatus
)

@Serializable
data class WorkerOverviewDto(
    val workerId: String,
    val name: String,
    val experienceInYears: Int,
    val mobileNumber: String,
    val dailyWage: Int,
    val advanceAmount: Int,
    val expertiseIn: List<ConstructionExpertise>,
    val joinedDate: Long,
    val advanceTookDate: Long?,
    val status: WorkerStatus
)

@Serializable
data class WorkersListOverviewDto(
    val workers: List<WorkerOverviewDto>,
    val nextPage: Int
)


@Serializable
data class WorkerIdDto(
    val workerId: String
)

fun Worker.toDto(): WorkerOverviewDto {
    return WorkerOverviewDto(
        workerId = this.id ?: "-",
        name = this.name,
        experienceInYears = this.experienceInYears,
        mobileNumber = this.mobileNumber,
        dailyWage = this.dailyWage,
        advanceAmount = this.advance,
        expertiseIn = this.expertiseIn,
        joinedDate = CommonUtils.convertLocalTimeToMillis(this.joinedDate),
        advanceTookDate = if (this.advanceTookDate != null) this.advanceTookDate.let {
            CommonUtils.convertLocalTimeToMillis(
                it
            )
        } else null,
        status = this.status
    )
}

fun List<Worker>.toWorkerDtoList(): List<WorkerOverviewDto> {
    return this.map { it.toDto() }
}