package com.handler.workers.karna.persistance

import com.handler.workers.karna.services.GeneralException
import com.handler.workers.karna.utils.ErrorCode

class PersistenceException(message: String) :
    GeneralException(ErrorCode.PERSISTENCE_FAILURE, message)