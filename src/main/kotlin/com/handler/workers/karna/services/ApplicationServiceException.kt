package com.handler.workers.karna.services

import com.handler.workers.karna.utils.ErrorCode

open class GeneralException(
    val errorCode: ErrorCode,
    override val message: String,
) : RuntimeException()

class ApplicationServiceException(errorCode: ErrorCode, data: String) :
    GeneralException(errorCode, data)