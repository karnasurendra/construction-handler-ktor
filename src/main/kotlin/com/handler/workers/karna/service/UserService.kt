package com.handler.workers.karna.service

import com.handler.workers.karna.entities.user.User
import org.litote.kmongo.Id

interface UserService {

    fun create(user: User): User

    fun findUserById(id: String): User?

    fun findAll(): List<User>

    fun findUserByNumberAndMPin(phoneNumber: Long, mPin: String): User?

    fun release()


}