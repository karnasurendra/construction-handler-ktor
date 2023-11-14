package com.handler.workers.karna.plugins

import com.handler.workers.karna.entities.user.User
import com.handler.workers.karna.entities.user.toDto
import com.handler.workers.karna.service.UserServiceImpl
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureUserRouting(userServiceImpl: UserServiceImpl) {

    routing {

        get("/user/list") {
            val usersList = userServiceImpl.findAll().map(User::toDto)
            call.respond(usersList)
        }

        authenticate {
            get("/user/list/operate") {
                val usersList = userServiceImpl.findAll().map(User::toDto)
                call.respond(usersList)
            }
        }

    }

}