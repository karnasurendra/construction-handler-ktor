package com.handler.workers.karna.domain

import org.bson.codecs.pojo.annotations.BsonId

data class Attendance(
    @BsonId
    val id: String = String(),
    val workerId: String = String(),
    val workerName:String = String(),
    val userId:String= String(),
    val attendedDate: Long
)