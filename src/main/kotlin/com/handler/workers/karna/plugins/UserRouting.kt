package com.handler.workers.karna.plugins

import com.handler.workers.karna.JwtConfig
import com.handler.workers.karna.entities.user.CreateUser
import com.handler.workers.karna.entities.user.User
import com.handler.workers.karna.entities.user.toDto
import com.handler.workers.karna.entities.user.toUser
import com.handler.workers.karna.service.UserService
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.utils.HashUtils
import com.handler.workers.karna.utils.Response
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserRouting(userService: UserService) {

    routing {

        post("/user/signup") {
            val userInfo = call.receive<CreateUser>()
            val user = userInfo.toUser()

            // Save the response to DB
            val createdUser = userService.create(user)

            if (createdUser.id != null) {
                val token = "Bearer ${JwtConfig.makeToken(createdUser.id.toString())}"
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

        get("/user/login") {
            val userInfo = call.receive<CreateUser>()

            println("Checking login user Info ${userInfo.phoneNumber} mPin ${userInfo.mPin}")
            val user = userService.findUserByNumberAndMPin(userInfo.phoneNumber, userInfo.mPin)
            println("Checking login user Info Id ${user?.id}  User ==  $user")
            if (user != null) {
                val token = "Bearer ${JwtConfig.makeToken(user.id.toString())}"
                val response: Response<String> =
                    Response(HttpStatusCode.OK.value, Constants.Messages.SUCCESSFULLY_LOGGED_IN, data = token)
                call.respond(response)
            } else {
                val response: Response<Nothing> =
                    Response(
                        HttpStatusCode.InternalServerError.value,
                        Constants.Messages.FAILED_TO_LOGIN,
                        error = Constants.Messages.INVALID_CREDENTIALS
                    )
                call.respond(response)
            }
        }

        authenticate {
            get("/user/info") {
                val usersList = userService.findAll().map(User::toDto)
                call.respond(usersList)
            }
        }

    }

}