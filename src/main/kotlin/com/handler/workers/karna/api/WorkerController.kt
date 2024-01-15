package com.handler.workers.karna.api

import com.handler.workers.karna.services.WorkerService
import com.handler.workers.karna.plugins.CustomPrincipal
import com.handler.workers.karna.plugins.SuccessResponseModel
import com.handler.workers.karna.plugins.handleFailureResponse
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement

interface WorkerController {

    suspend fun addWorker(call: ApplicationCall)

    suspend fun deleteWorker(call: ApplicationCall)

    suspend fun getWorkerInfo(call: ApplicationCall)

    suspend fun getWorkersList(call: ApplicationCall)

}

class WorkerControllerImpl(private val workerService: WorkerService) : WorkerController {
    override suspend fun addWorker(call: ApplicationCall) {
        try {
            val principal = call.authentication.principal<CustomPrincipal>()
            val user = principal?.user

            if (user?.id != null) {
                val userId = user.id
                val workerDto = call.receive<WorkerDto>()
                when (val result = workerService.addWorker(userId, workerDto)) {
                    is ApiResult.Failure -> {
                        handleFailureResponse(call, result)
                    }

                    is ApiResult.Success -> {
                        val successResponseModel = SuccessResponseModel(
                            HttpStatusCode.Created.value,
                            "Worker added successfully",
                            data = Json.encodeToJsonElement(WorkerIdDto(result.value)) as JsonObject
                        )
                        call.respond(successResponseModel)
                    }
                }
            } else {
                handleFailureResponse(
                    call,
                    ApiResult.Failure(ErrorCode.AUTHENTICATION_FAILURE, "Something went wrong!")
                )
            }


        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun deleteWorker(call: ApplicationCall) {
        try {
            val workerId = call.parameters["id"]

            if (workerId != null) {
                when (val result = workerService.deleteWorker(workerId)) {
                    is ApiResult.Failure -> {
                        return handleFailureResponse(
                            call,
                            ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Failed to delete worker")
                        )
                    }

                    is ApiResult.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            SuccessResponseModel(HttpStatusCode.OK.value, "Deleted Successfully")
                        )
                    }
                }
            } else {
                return handleFailureResponse(
                    call,
                    ApiResult.Failure(ErrorCode.MISSING_PARAMETER, "Worker id is not available")
                )
            }

        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun getWorkerInfo(call: ApplicationCall) {
        try {
            val workerId = call.parameters["id"]

            if (workerId != null) {
                when (val result = workerService.getSingleWorker(workerId)) {
                    is ApiResult.Failure -> {
                        handleFailureResponse(
                            call,
                            ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Failed to fetch worker info.")
                        )
                    }

                    is ApiResult.Success -> {
                        call.respond(
                            HttpStatusCode.OK,
                            SuccessResponseModel(
                                HttpStatusCode.OK.value,
                                "User info fetched successfully",
                                Json.encodeToJsonElement(result.value.toDto()) as JsonObject
                            ),
                        )
                    }
                }
            } else {
                handleFailureResponse(call, ApiResult.Failure(ErrorCode.UNKNOWN_ERROR, "Failed to fetch info."))
            }

        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun getWorkersList(call: ApplicationCall) {
        try {
            val principal = call.authentication.principal<CustomPrincipal>()
            val user = principal?.user

            val page = call.parameters["page"]?.toIntOrNull() ?: 1
            val count = call.parameters["count"]?.toIntOrNull() ?: 3

            if (user?.id != null) {
                when (val result = workerService.getWorkersList(user.id, page, count)) {
                    is ApiResult.Failure -> {
                        handleFailureResponse(call, result)
                    }

                    is ApiResult.Success -> {
                        val mList = result.value.toWorkerDtoList()

                        /**While reading from we are doing +1, which helps to identify the availability of next page*/
                        val nextPage = if (mList.size > count) {
                            mList.removeLast()
                            page + 1
                        } else {
                            0
                        }
                        val returnResponse = WorkersListOverviewDto(mList, nextPage)
                        call.respond(
                            HttpStatusCode.OK, SuccessResponseModel(
                                HttpStatusCode.OK.value,
                                "Workers list fetched successfully",
                                Json.encodeToJsonElement(returnResponse) as JsonObject
                            )
                        )
                    }
                }
            } else {
                handleFailureResponse(
                    call,
                    ApiResult.Failure(ErrorCode.AUTHENTICATION_FAILURE, "Something went wrong!")
                )
            }


        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

}