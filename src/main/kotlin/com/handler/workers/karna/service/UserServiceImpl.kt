package com.handler.workers.karna.service

import com.handler.workers.karna.entities.user.User
import com.handler.workers.karna.utils.HashUtils
import com.mongodb.client.model.Filters
import org.bson.conversions.Bson
import org.bson.types.ObjectId
import org.litote.kmongo.*
import org.litote.kmongo.id.toId

class UserServiceImpl : UserService {

    private val client = KMongo.createClient()
    private val database = client.getDatabase("user")
    private val userCollection = database.getCollection<User>()

    override fun create(user: User): User {
        userCollection.insertOne(user)
        return user
    }

    override fun findAll(): List<User> = userCollection.find().toList()

    override fun findUserById(id: String): User? {
        val bsonId: Id<User> = ObjectId(id).toId()
        return userCollection.findOne(User::id eq bsonId)
    }

    override fun findUserByNumber(phoneNumber: Long): User? {
        return userCollection.findOne(User::phoneNumber eq phoneNumber)
    }

    override fun findUserByNumberAndMPin(phoneNumber: Long, mPin: String): User? {
        val user = userCollection.findOne(User::phoneNumber eq phoneNumber)
        return if (user != null && HashUtils.verifyHash(user.pin, mPin)) {
            user
        } else {
            null
        }
    }

    override fun release() {
        client.close()
    }

}