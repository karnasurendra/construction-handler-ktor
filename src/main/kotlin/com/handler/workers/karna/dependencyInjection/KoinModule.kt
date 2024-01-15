package com.handler.workers.karna.dependencyInjection

import com.handler.workers.karna.api.UserController
import com.handler.workers.karna.api.UserControllerImpl
import com.handler.workers.karna.api.WorkerController
import com.handler.workers.karna.api.WorkerControllerImpl
import com.handler.workers.karna.services.UserService
import com.handler.workers.karna.services.UserServiceImpl
import com.handler.workers.karna.services.WorkerService
import com.handler.workers.karna.services.WorkerServiceImpl
import com.handler.workers.karna.persistance.UserRepository
import com.handler.workers.karna.persistance.UserRepositoryImpl
import com.handler.workers.karna.persistance.WorkerRepository
import com.handler.workers.karna.persistance.WorkerRepositoryImpl
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.validations.UserDtoValidator
import com.handler.workers.karna.validations.WorkerDtoValidator
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

    single { WorkerDtoValidator() }
    single<WorkerRepository> { WorkerRepositoryImpl(get()) }
    single<WorkerService> { WorkerServiceImpl(get(), get()) }
    single<WorkerController> { WorkerControllerImpl(get()) }
}

fun Application.setUpKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(encryptionServerModule)
    }
}
