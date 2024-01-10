package com.handler.workers.karna.application

import com.handler.workers.karna.utils.ErrorCode
import io.ktor.http.*

open class GeneralException(
    val errorCode: ErrorCode,
    override val message: String,
) : RuntimeException()

class ApplicationServiceException(errorCode: ErrorCode, data: String) :
    GeneralException(errorCode, data)