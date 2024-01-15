package com.handler.workers.karna.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object CommonUtils {

    fun convertMillisToLocalDateTime(millis: Long): LocalDateTime {
        val instant = Instant.ofEpochMilli(millis)
        return LocalDateTime.ofInstant(instant, ZoneOffset.UTC) // You can use a different ZoneOffset if needed
    }

    fun convertLocalTimeToMillis(localDateTime: LocalDateTime): Long {
        return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli()
    }

}