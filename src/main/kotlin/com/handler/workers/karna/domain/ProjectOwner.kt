package com.handler.workers.karna.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class ProjectOwner(
    @BsonId
    val id: Id<ProjectOwner>? = null,
    val name: String,
    val phoneNumber: Long
)
