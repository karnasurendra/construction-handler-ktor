package com.handler.workers.karna.service

import com.handler.workers.karna.entities.user.User

interface UserService {

    fun findUserById(id: String): User?

    fun findAll():List<User>

    fun release()

}