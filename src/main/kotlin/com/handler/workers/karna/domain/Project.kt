package com.handler.workers.karna.domain

import org.bson.codecs.pojo.annotations.BsonId
import org.litote.kmongo.Id

data class Project(
    @BsonId
    val id: Id<Project>? = null,
    val name: String,
    val location: Pair<Double, Double>,
    val projectOwner: String,
    val budget: Double,
    val createdBy: String,
    val advance: Double?
)
