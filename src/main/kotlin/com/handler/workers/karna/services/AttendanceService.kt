package com.handler.workers.karna.services

import com.handler.workers.karna.api.AttendanceDto
import com.handler.workers.karna.persistance.AttendanceRepository
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.validations.AttendanceValidator

interface AttendanceService {
    suspend fun updateAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String>

    suspend fun getAttendance(userId: String, workerId:String): ApiResult<String>
}

class AttendanceServiceImpl(
    private val attendanceRepository: AttendanceRepository,
    private val attendanceValidator: AttendanceValidator
) : AttendanceService {
    override suspend fun updateAttendance(userId: String, attendanceDto: AttendanceDto): ApiResult<String> {
        TODO("Not yet implemented")
    }

    override suspend fun getAttendance(userId: String, workerId:String): ApiResult<String> {
        TODO("Not yet implemented")
    }

}