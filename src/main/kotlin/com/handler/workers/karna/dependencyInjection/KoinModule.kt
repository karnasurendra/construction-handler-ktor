package com.handler.workers.karna.dependencyInjection

import com.handler.workers.karna.api.*
import com.handler.workers.karna.persistance.*
import com.handler.workers.karna.services.*
import com.handler.workers.karna.utils.Constants
import com.handler.workers.karna.validations.AttendanceValidator
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

    single { AttendanceValidator() }
    single<AttendanceRepository> { AttendanceRepositoryImpl(get()) }
    single<AttendanceService> { AttendanceServiceImpl(get(), get()) }
    single<AttendanceController> { AttendanceControllerImpl(get()) }
}

fun Application.setUpKoin() {
    install(Koin) {
        SLF4JLogger()
        modules(encryptionServerModule)
    }
}
