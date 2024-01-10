package com.handler.workers.karna.security

import com.handler.workers.karna.application.GeneralException
import com.handler.workers.karna.utils.ErrorCode

class EncryptionException(override val message: String) :
    GeneralException(ErrorCode.ENCRYPTION_FAILURE, message)