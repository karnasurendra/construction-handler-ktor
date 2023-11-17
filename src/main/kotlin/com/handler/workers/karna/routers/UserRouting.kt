package com.handler.workers.karna.routers

import CustomPrincipal
import com.handler.workers.karna.JwtConfig
import com.handler.workers.karna.entities.user.*
import com.handler.workers.karna.service.UserService
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.utils.Response
import com.handler.workers.karna.validations.UserValidations
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserRouting(userService: UserService, userValidations: UserValidations = UserValidations()) {

    routing {

        post("/user/signup") {
            val userInfo = call.receive<CreateUser>()
            val user = userInfo.toUser()

            val validationResponse = userValidations.validateUser(userInfo)

            if (!validationResponse.isValid) {
                val response: Response<Nothing> =
                    Response(
                        HttpStatusCode.BadRequest.value,
                        validationResponse.message
                    )
                call.respond(response)
                return@post
            }

            val isUserExist = userService.findUserByNumber(userInfo.phoneNumber)?.let {
                true
            } ?: false

            if (isUserExist) {
                val response: Response<Nothing> =
                    Response(
                        HttpStatusCode.BadRequest.value,
                        Constants.Messages.ALREADY_SIGNUP
                    )
                call.respond(response)
            } else {
                // Save the response to DB
                val createdUser = userService.create(user)

                if (createdUser.id != null) {
                    val token = JwtConfig.makeToken(createdUser.id.toString())
                    val response: Response<String> =
                        Response(HttpStatusCode.OK.value, Constants.Messages.SUCCESSFULLY_SIGNUP, data = token)
                    call.respond(response)
                } else {
                    val response: Response<Nothing> =
                        Response(
                            HttpStatusCode.InternalServerError.value,
                            Constants.Messages.FAILED_TO_SIGNUP
                        )
                    call.respond(response)
                }
            }
        }

        get("/user/login") {
            val userInfo = call.receive<CreateUser>()

            val validationResponse = userValidations.validateNumberAndPinOnly(userInfo)

            if (!validationResponse.isValid) {
                val response: Response<Nothing> =
                    Response(
                        HttpStatusCode.BadRequest.value,
                        validationResponse.message
                    )
                call.respond(response)
                return@get
            }

            val user = userService.findUserByNumberAndMPin(userInfo.phoneNumber, userInfo.pin)
            if (user != null) {
                val token = JwtConfig.makeToken(user.id.toString())
                val response: Response<String> =
                    Response(HttpStatusCode.OK.value, Constants.Messages.SUCCESSFULLY_LOGGED_IN, data = token)
                call.respond(response)
            } else {
                val response: Response<Nothing> =
                    Response(
                        HttpStatusCode.InternalServerError.value,
                        Constants.Messages.INVALID_CREDENTIALS
                    )
                call.respond(response)
            }
        }

        authenticate {
            get("/user/info") {
                val principal = call.authentication.principal<CustomPrincipal>()
                val user = principal?.user
                if (user != null) {

                    val returnUserInfo = user.toReturnUserInfo()

                    val response: Response<ReturnUserInfo> =
                        Response(
                            HttpStatusCode.OK.value,
                            Constants.ValidationMessages.SUCCESS,
                            data = returnUserInfo
                        )
                    call.respond(response)
                } else {
                    val response: Response<Nothing> =
                        Response(
                            HttpStatusCode.OK.value,
                            Constants.ValidationMessages.FAILED_TO_FETCH_USER_INFO
                        )
                    call.respond(response)
                }
            }
        }

    }

}