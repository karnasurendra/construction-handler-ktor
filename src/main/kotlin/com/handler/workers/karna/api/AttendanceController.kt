package com.handler.workers.karna.api

import com.handler.workers.karna.plugins.CustomPrincipal
import com.handler.workers.karna.plugins.SuccessResponseModel
import com.handler.workers.karna.plugins.handleFailureResponse
import com.handler.workers.karna.services.AttendanceService
import com.handler.workers.karna.services.GeneralException
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.*

interface AttendanceController {

    suspend fun updateAttendance(call: ApplicationCall)

    suspend fun getAttendance(call: ApplicationCall)

    suspend fun deleteAttendance(call: ApplicationCall)

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
                        handleFailureResponse(call, result)
                    }

                    is ApiResult.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            SuccessResponseModel(
                                statusCode = HttpStatusCode.OK.value,
                                message = result.value
                            )
                        )
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
                val rangeFrom = call.parameters["rangeFrom"]?.toLongOrNull() ?: 0L
                val rangeTo = call.parameters["rangeTo"]?.toLongOrNull() ?: 0L

                when (val result = attendanceService.getAttendance(userId, rangeFrom, rangeTo)) {
                    is ApiResult.Failure -> {
                        handleFailureResponse(call, result)
                    }

                    is ApiResult.Success -> {
                        val attendanceList = AttendanceListDto(attendanceList = result.value.toAttendanceDtoList())
                        call.respond(
                            HttpStatusCode.OK, SuccessResponseModel(
                                statusCode = HttpStatusCode.OK.value,
                                message = "Attendance list fetched  successfully.",
                                data = Json.encodeToJsonElement(attendanceList) as JsonObject
                            )
                        )
                    }
                }
            } else {
                handleFailureResponse(call, ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Something went wrong."))
            }
        } catch (e: GeneralException) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun deleteAttendance(call: ApplicationCall) {
        try {
            val principal = call.authentication.principal<CustomPrincipal>()
            val user = principal?.user
            if (user?.id != null) {
                val id = call.parameters["id"] ?: ""

                when (val result = attendanceService.deleteAttendance(user.id, id)) {
                    is ApiResult.Failure -> {
                        handleFailureResponse(call, result)
                    }

                    is ApiResult.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            SuccessResponseModel(
                                statusCode = HttpStatusCode.OK.value,
                                message = "Removed attendance successfully"
                            )
                        )
                    }
                }
            } else {
                handleFailureResponse(call, ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Something went wrong."))
            }

        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

}