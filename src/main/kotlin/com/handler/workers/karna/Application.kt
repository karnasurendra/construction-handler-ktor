package com.handler.workers.karna

import com.handler.workers.karna.configuration.setupApplicationConfiguration
import com.handler.workers.karna.dependencyInjection.setUpKoin
import com.handler.workers.karna.plugins.configureCallLogging
import com.handler.workers.karna.plugins.configureRouting
import com.handler.workers.karna.plugins.configureSecurity
import com.handler.workers.karna.utils.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) = io.ktor.server.netty.EngineMain.main(args)

/**
 * constructionModule we set in application.conf file
 * */
fun Application.constructionModule() {

    /*
    * security [ partially done ]
    */

    setupApplicationConfiguration()
    setUpKoin()
    configureSerialization()
    configureSecurity()
    configureRouting()
    configureCallLogging()

    environment.monitor.subscribe(ApplicationStarted) { application ->
        application.environment.log.info("--- Server is started ---")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        application.environment.log.info("--- Server is stopped ---")
        application.environment.monitor.unsubscribe(ApplicationStarted) {}
        application.environment.monitor.unsubscribe(ApplicationStopped) {}
    }
}