package com.handler.workers.karna.services

import com.handler.workers.karna.api.WorkerDto
import com.handler.workers.karna.domain.Worker
import com.handler.workers.karna.persistance.WorkerRepository
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.CommonUtils
import com.handler.workers.karna.utils.ErrorCode
import com.handler.workers.karna.validations.WorkerDtoValidator

interface WorkerService {

    suspend fun addWorker(userId: String, workerDto: WorkerDto): ApiResult<String>

    suspend fun getWorkersList(userId: String, page: Int, count: Int): ApiResult<List<Worker>>

    suspend fun deleteWorker(workerId: String): ApiResult<String>

    suspend fun getSingleWorker(workerId: String): ApiResult<Worker>

}

class WorkerServiceImpl(
    private val workerDtoValidator: WorkerDtoValidator,
    private val workerRepository: WorkerRepository
) : WorkerService {
    override suspend fun addWorker(userId: String, workerDto: WorkerDto): ApiResult<String> {
        try {

            val validationResult = workerDtoValidator.validateWorker(workerDto)
            if (validationResult.isNotEmpty()) {
                throw ApplicationServiceException(
                    ErrorCode.MISSING_PARAMETER,
                    validationResult.joinToString(",")
                )
            }

            val worker = workerRepository.saveWorker(
                Worker(
                    name = workerDto.name,
                    experienceInYears = workerDto.experienceInYears,
                    mobileNumber = workerDto.mobileNumber,
                    dailyWage = workerDto.dailyWage,
                    advance = workerDto.advanceAmount ?: 0,
                    expertiseIn = workerDto.expertiseIn,
                    userId = userId,
                    joinedDate = CommonUtils.convertMillisToLocalDateTime(workerDto.joinedDate),
                    advanceTookDate = if (workerDto.advanceAmount != null && workerDto.advanceAmount != 0)
                        workerDto.advanceTookDate?.let { CommonUtils.convertMillisToLocalDateTime(it) } else null,
                    status = workerDto.status
                )
            )

            return if (worker.id != null) {
                ApiResult.Success(worker.id)
            } else {
                ApiResult.Failure(ErrorCode.PERSISTENCE_FAILURE, "Failed to add worker")
            }

        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }
    }

    override suspend fun getWorkersList(userId: String, page: Int, count: Int): ApiResult<List<Worker>> {
        try {
            val mList = workerRepository.getWorkersList(userId, page, count)
            return ApiResult.Success(mList)
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }
    }

    override suspend fun deleteWorker(workerId: String): ApiResult<String> {
        try {
            workerRepository.deleteWorker(workerId)
            return ApiResult.Success("Deleted successfully.")
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }
    }

    override suspend fun getSingleWorker(workerId: String): ApiResult<Worker> {
        try {
            val worker = workerRepository.findWorkerById(workerId)
            return if (worker != null) {
                ApiResult.Success(worker)
            } else {
                ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Failed to fetch worker info")
            }
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }
    }

}