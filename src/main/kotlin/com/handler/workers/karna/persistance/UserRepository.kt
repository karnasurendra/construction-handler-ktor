package com.handler.workers.karna.persistance

import com.handler.workers.karna.domain.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

interface UserRepository {
    suspend fun findBy(mobileNumber: String): User?
    suspend fun save(user: User): String?
    suspend fun findUserById(id: String): User?
}

class UserRepositoryImpl(private val database: CoroutineDatabase) : UserRepository {

    private val userCollection = database.getCollection<User>()
    override suspend fun findBy(mobileNumber: String): User? = withContext(Dispatchers.IO) {
        try {
            val query = User::mobileNumber eq mobileNumber
            userCollection.findOne(query) ?: return@withContext null
        } catch (e: Exception) {
            throw PersistenceException("Failed to find with username '$mobileNumber' in database.")
        }
    }

    override suspend fun save(user: User): String? = withContext(Dispatchers.IO) {
        try {
            userCollection.insertOne(user)
            user.id
        } catch (e: Exception) {
            throw PersistenceException("Failed to save.")
        }
    }

    override suspend fun findUserById(id: String): User? = withContext(Dispatchers.IO) {
        try {
            val query = User::id eq id
            userCollection.findOne(query) ?: return@withContext null
        } catch (e: Exception) {
            throw PersistenceException("Failed to find with user '$id' in database.")
        }
    }

}