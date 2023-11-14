package com.handler.workers.karna

import authenticate
import com.handler.workers.karna.plugins.configureSerialization
import com.handler.workers.karna.plugins.configureUserRouting
import com.handler.workers.karna.service.UserServiceImpl
import io.ktor.server.application.*


fun Application.userModule() {
    configureSerialization()
    val userService = UserServiceImpl()
    authenticate(userService)

    configureUserRouting(userService)

    environment.monitor.subscribe(ApplicationStarted) { application ->
        application.environment.log.info("--- Server is started ---")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        application.environment.log.info("--- Server is stopped ---")
        userService.release()
        application.environment.monitor.unsubscribe(ApplicationStarted) {}
        application.environment.monitor.unsubscribe(ApplicationStopped) {}
    }
}
