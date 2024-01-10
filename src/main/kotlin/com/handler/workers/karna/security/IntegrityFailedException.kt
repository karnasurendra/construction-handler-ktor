package com.handler.workers.karna.security

import com.handler.workers.karna.application.GeneralException
import com.handler.workers.karna.utils.ErrorCode

class IntegrityFailedException(
    override val message: String,
) : GeneralException(ErrorCode.INTEGRITY_CHECK_FAILED, message)