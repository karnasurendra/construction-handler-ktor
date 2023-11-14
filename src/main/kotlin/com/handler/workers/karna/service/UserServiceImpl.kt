package com.handler.workers.karna.service

import com.handler.workers.karna.entities.user.User
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

class UserServiceImpl : UserService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("user")
    private val userCollection = database.getCollection<User>()

    override fun findAll(): List<User> = userCollection.find().toList()

    override fun findUserById(id: String): User? {
        val bsonId: Id<User> = ObjectId(id).toId()
        return userCollection.findOne(User::id eq bsonId)
    }

    override fun release() {
        client.close()
    }

}