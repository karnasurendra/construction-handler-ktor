package com.handler.workers.karna

import com.handler.workers.karna.configuration.ApplicationConfiguration
import com.handler.workers.karna.configuration.setupApplicationConfiguration
import com.handler.workers.karna.dependencyInjection.setUpKoin
import com.handler.workers.karna.plugins.configureCallLogging
import com.handler.workers.karna.plugins.configureRouting
import com.handler.workers.karna.plugins.configureSecurity
import com.handler.workers.karna.utils.configureSerialization
import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore

fun main(args: Array<String>) = EngineMain.main(args)

/**
 * constructionModule we set in application.conf file
 * */
fun Application.constructionModule() {

    setupApplicationConfiguration()

    embeddedServer(Netty, applicationEngineEnvironment {
        val keyStore = loadKeyStore(
            ApplicationConfiguration.ssl.keystorePath,
            ApplicationConfiguration.ssl.keyStorePassword
        )

        sslConnector(
            keyStore = keyStore,
            keyAlias = ApplicationConfiguration.ssl.keyAlias,
            keyStorePassword = { ApplicationConfiguration.ssl.keyStorePassword.toCharArray() },
            privateKeyPassword = { ApplicationConfiguration.ssl.privateKeyPassword.toCharArray() }
        ) {

            port = ApplicationConfiguration.ssl.port

            module {
                setUpKoin()
                configureSerialization()
                configureSecurity()
                configureRouting()
                configureCallLogging()
            }
        }
    }).start(true)

    environment.monitor.subscribe(ApplicationStarted) { application ->
        application.environment.log.info("--- Server is started ---")
    }

    environment.monitor.subscribe(ApplicationStopped) { application ->
        application.environment.log.info("--- Server is stopped ---")
        application.environment.monitor.unsubscribe(ApplicationStarted) {}
        application.environment.monitor.unsubscribe(ApplicationStopped) {}
    }
}

fun loadKeyStore(keystorePath: String, keystorePassword: String): KeyStore {
    val keyStore = KeyStore.getInstance("JKS")
    val keystoreFile = File(keystorePath)

    FileInputStream(keystoreFile).use { fileInputStream ->
        keyStore.load(fileInputStream, keystorePassword.toCharArray())
    }

    return keyStore
}



