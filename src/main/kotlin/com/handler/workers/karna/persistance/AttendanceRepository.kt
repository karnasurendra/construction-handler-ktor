package com.handler.workers.karna.persistance

import com.handler.workers.karna.validations.AttendanceValidator
import org.litote.kmongo.coroutine.CoroutineDatabase

interface AttendanceRepository {

}

class AttendanceRepositoryImpl(
    private val database: CoroutineDatabase
) : AttendanceRepository {

}