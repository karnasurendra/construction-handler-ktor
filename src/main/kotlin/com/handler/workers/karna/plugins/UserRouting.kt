package com.handler.workers.karna.plugins

import com.handler.workers.karna.api.UserController
import com.handler.workers.karna.utils.Constants
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Routing.configureUserRouting() {

    val userController by inject<UserController>()

    route(Constants.ApiNames.USER_BASE) {

        post(Constants.ApiNames.USER_CREATE) {
            userController.createNewUser(call)
        }

        post(Constants.ApiNames.USER_LOGIN) {
            userController.login(call)
        }

        authenticate {

            get(Constants.ApiNames.USER_INFO) {
                println("+++++++++++++++++++++++++")
                userController.getUserInfo(call)
            }

        }

    }
}
