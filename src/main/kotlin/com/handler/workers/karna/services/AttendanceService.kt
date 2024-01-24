package com.handler.workers.karna.services

import com.handler.workers.karna.api.AttendanceDto
import com.handler.workers.karna.api.fromDto
import com.handler.workers.karna.domain.Attendance
import com.handler.workers.karna.persistance.AttendanceRepository
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode
import com.handler.workers.karna.validations.AttendanceValidator

interface AttendanceService {
    suspend fun updateAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String>

    suspend fun getAttendance(userId: String, rangeFrom: Long, rangeTo: Long): ApiResult<List<Attendance>>

    suspend fun deleteAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String>
}

class AttendanceServiceImpl(
    private val attendanceRepository: AttendanceRepository,
    private val attendanceValidator: AttendanceValidator
) : AttendanceService {
    override suspend fun updateAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String> {
        try {

            val attendanceValidations = attendanceValidator.validateAttendanceDto(attendanceDto)

            if (attendanceValidations.isNotEmpty()) {
                return ApiResult.Failure(ErrorCode.MISSING_PARAMETER, attendanceValidations.joinToString(","))
            } else {

                val worker = attendanceRepository.isWorkerOwnerIsUser(attendanceDto.workerId, userId)

                if (worker == null) {
                    return ApiResult.Failure(
                        ErrorCode.AUTHORIZATION_FAILURE,
                        "No permission to update this worker info."
                    )
                } else {
                    attendanceRepository.addUserToAttendance(attendanceDto.fromDto())
                    return ApiResult.Success("Attendance updated successfully.")
                }

            }

        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.localizedMessage)
        }
    }

    override suspend fun getAttendance(userId: String, rangeFrom: Long, rangeTo: Long): ApiResult<List<Attendance>> {
        try {
            val list = attendanceRepository.getAllWorkersInRange(userId, rangeFrom, rangeTo)
            return ApiResult.Success(list)
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.localizedMessage)
        }
    }

    override suspend fun deleteAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String> {
        try {
            val attendanceValidations = attendanceValidator.validateAttendanceDto(attendanceDto)

            if (attendanceValidations.isNotEmpty()) {
                return ApiResult.Failure(ErrorCode.MISSING_PARAMETER, attendanceValidations.joinToString(","))
            } else {

                val worker = attendanceRepository.isWorkerOwnerIsUser(attendanceDto.workerId, userId)

                if (worker == null) {
                    return ApiResult.Failure(
                        ErrorCode.AUTHORIZATION_FAILURE,
                        "No permission to update this worker info."
                    )
                } else {
                    attendanceRepository.removeUserFromAttendance(attendanceDto.fromDto())
                    return ApiResult.Success("Attendance updated successfully.")
                }

            }
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.localizedMessage)
        }
    }

}