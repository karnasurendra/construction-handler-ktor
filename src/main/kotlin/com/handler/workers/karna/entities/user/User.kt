package com.handler.workers.karna.entities.user

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class User(
    @BsonId
    val id: Id<User>? = null,
    val phoneNumber: Long,
    val mPin: String,
    val name: String
)
