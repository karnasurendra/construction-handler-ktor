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

    override fun findUserByNumberAndMPin(phoneNumber: Long, mPin: String): User? {
        println("Checking findUserByNumberAndMPin == --- $mPin ==  $phoneNumber")
        return try {
            val user = userCollection.findOne(User::phoneNumber eq phoneNumber)
            println("Checking findUserByNumberAndMPin from  DB --- $user")
            return if (user != null && HashUtils.verifyHash(user.mPin, mPin)) {
                user
            } else {
                null
            }
        } catch (e: Exception) {
            println("Checking findUserByNumberAndMPin Exception ---  ${e.localizedMessage}")
            null
        }

    }

    override fun release() {
        client.close()
    }

}