package com.handler.workers.karna

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun startServer() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::userModule)
        .start(wait = true)
}