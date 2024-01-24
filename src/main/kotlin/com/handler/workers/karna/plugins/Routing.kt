package com.handler.workers.karna.plugins

import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        configureUserRouting()
        configureWorkerRouting()
        configureAttendanceRouting()
    }
}

suspend fun handleFailureResponse(call: ApplicationCall, failure: ApiResult.Failure) {
    val httpStatusCode = when (failure.errorCode) {
        NOT_ACCEPTED_MIME_TYPE,
        DESERIALIZATION_ERROR,
        MISSING_PARAMETER, INPUT_VALIDATION_FAILED, AUTHORIZATION_FAILURE -> HttpStatusCode.BadRequest

        else -> HttpStatusCode.InternalServerError
    }
    val responseModel = FailureResponseModel(httpStatusCode.value, failure.errorMessage)
    call.respond(status = httpStatusCode, message = responseModel)
}

