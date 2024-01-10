package com.handler.workers.karna.plugins

import com.handler.workers.karna.application.UserService
import com.handler.workers.karna.domain.User
import com.handler.workers.karna.security.JWTConfig
import com.handler.workers.karna.utils.ApiResult
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import org.koin.ktor.ext.inject

fun Application.configureSecurity() {

    val userService by inject<UserService>()


    install(StatusPages) {
        status(HttpStatusCode.Unauthorized) { call, _ ->
            call.respond(
                HttpStatusCode.Unauthorized,
                FailureResponseModel(HttpStatusCode.Unauthorized.value, "UnAuthorized")
            )
        }
    }

    authentication {
        jwt {
            realm = "ktor.io"
            verifier(
                JWTConfig.verifier
            )
            validate { credential ->
                val id = credential.payload.getClaim("id").asString()


                if (id != null) {
                    when (val result = userService.findById(id)) {
                        is ApiResult.Failure -> {
                            null
                        }

                        is ApiResult.Success -> {
                            println("+++++++++++++++++++++++++")
                            CustomPrincipal(result.value)
                        }
                    }

                } else {
                    null
                }
            }
        }
    }


}

data class CustomPrincipal(val user: User) : Principal
