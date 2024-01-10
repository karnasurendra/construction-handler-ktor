package com.handler.workers.karna.dependencyInjection

import com.handler.workers.karna.api.UserController
import com.handler.workers.karna.api.UserControllerImpl
import com.handler.workers.karna.application.UserService
import com.handler.workers.karna.application.UserServiceImpl
import com.handler.workers.karna.persistance.UserRepository
import com.handler.workers.karna.persistance.UserRepositoryImpl
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.validations.UserDtoValidator
import io.ktor.server.application.*
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.koin.logger.SLF4JLogger
import org.litote.kmongo.coroutine.CoroutineClient
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

val encryptionServerModule = module {

    single<CoroutineClient> {
        KMongo.createClient().coroutine
    }

    single<CoroutineDatabase> {
        val client: CoroutineClient = get()
        client.getDatabase(Constants.DBConstants.DB_NAME)
    }

    single { UserDtoValidator() }
    single<UserRepository> { UserRepositoryImpl(get()) }
    single<UserService> { UserServiceImpl(get(), get()) }
    single<UserController> { UserControllerImpl(get()) }
}

fun Application.setUpKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(encryptionServerModule)
    }
}
