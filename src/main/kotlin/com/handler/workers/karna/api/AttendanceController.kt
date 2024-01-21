package com.handler.workers.karna.api

import com.handler.workers.karna.plugins.CustomPrincipal
import com.handler.workers.karna.plugins.handleFailureResponse
import com.handler.workers.karna.services.AttendanceService
import com.handler.workers.karna.services.GeneralException
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*

interface AttendanceController {

    suspend fun updateAttendance(call: ApplicationCall)

    suspend fun getAttendance(call: ApplicationCall)

}


class AttendanceControllerImpl(private val attendanceService: AttendanceService) : AttendanceController {
    override suspend fun updateAttendance(call: ApplicationCall) {
        try {

            val principal = call.authentication.principal<CustomPrincipal>()
            val user = principal?.user

            if (user?.id != null) {
                val userId = user.id
                val attendanceDto = call.receive<AttendanceDto>()

                when (val result = attendanceService.updateAttendance(userId, attendanceDto)) {
                    is ApiResult.Failure -> {

                    }

                    is ApiResult.Success -> {

                    }
                }
            } else {
                handleFailureResponse(call, ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Something went wrong."))
            }


        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun getAttendance(call: ApplicationCall) {
        try {
            val principal = call.authentication.principal<CustomPrincipal>()
            val user = principal?.user

            if (user?.id != null) {
                val userId = user.id
                val workerId = call.parameters["id"] ?: ""

                when (val result = attendanceService.getAttendance(userId, workerId)) {
                    is ApiResult.Failure -> {

                    }

                    is ApiResult.Success -> {

                    }
                }
            } else {
                handleFailureResponse(call, ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Something went wrong."))
            }
        } catch (e: GeneralException) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

}