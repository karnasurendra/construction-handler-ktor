package com.handler.workers.karna.api

import com.handler.workers.karna.services.UserService
import com.handler.workers.karna.plugins.CustomPrincipal
import com.handler.workers.karna.plugins.SuccessResponseModel
import com.handler.workers.karna.plugins.TokenResponse
import com.handler.workers.karna.plugins.handleFailureResponse
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.utils.ErrorCode
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.encodeToJsonElement
import java.time.ZoneOffset

interface UserController {
    suspend fun createNewUser(call: ApplicationCall)
    suspend fun getUserInfo(call: ApplicationCall)
    suspend fun login(call: ApplicationCall)

}


class UserControllerImpl(
    private val userService: UserService
) : UserController {
    override suspend fun createNewUser(call: ApplicationCall) {
        try {
            val userDto = call.receive<UserDto>()
            when (val result = userService.save(userDto)) {
                is ApiResult.Success -> {
                    call.respond(
                        HttpStatusCode.Created,
                        SuccessResponseModel(
                            statusCode = HttpStatusCode.Created.value,
                            message = Constants.Messages.USER_CREATED_SUCCESSFULLY,
                            data = Json.encodeToJsonElement(TokenResponse(result.value)) as JsonObject
                        )
                    )
                }

                is ApiResult.Failure -> {
                    handleFailureResponse(call, result)
                }
            }
        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

    override suspend fun getUserInfo(call: ApplicationCall) {
        val principal = call.authentication.principal<CustomPrincipal>()
        val user = principal?.user
        if (user != null) {
            call.respond(
                HttpStatusCode.OK,
                SuccessResponseModel(
                    statusCode = HttpStatusCode.OK.value,
                    message = Constants.Messages.USER_INFO_SUCCESSFULLY_FETCHED,
                    data = Json.encodeToJsonElement(
                        UserOverviewDto(
                            user.username,
                            user.mobileNumber,
                            user.created.toInstant(ZoneOffset.UTC).toEpochMilli(),
                            user.experienceInYears,
                            user.expertiseIn
                        )
                    ) as JsonObject
                )
            )
        } else {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.AUTHENTICATION_FAILURE, "Something went wrong!"))
        }
    }

    override suspend fun login(call: ApplicationCall) {
        try {
            val userLoginDto = call.receive<UserLoginDto>()
            when (val result = userService.login(userLoginDto)) {
                is ApiResult.Failure -> {
                    handleFailureResponse(call, result)
                }

                is ApiResult.Success -> {
                    call.respond(
                        HttpStatusCode.OK, SuccessResponseModel(
                            statusCode = HttpStatusCode.Created.value,
                            message = Constants.Messages.SUCCESSFULLY_LOGGED_IN,
                            data = Json.encodeToJsonElement(TokenResponse(result.value)) as JsonObject
                        )
                    )
                }
            }
        } catch (e: Exception) {
            handleFailureResponse(call, ApiResult.Failure(ErrorCode.DESERIALIZATION_ERROR, e.localizedMessage))
        }
    }

}