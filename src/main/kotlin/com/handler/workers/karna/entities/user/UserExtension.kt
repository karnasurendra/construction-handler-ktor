package com.handler.workers.karna.entities.user

import com.handler.workers.karna.utils.HashUtils


fun User.toDto(): CreateUser = CreateUser(
    id = this.id.toString(),
    name = this.name,
    pin = this.pin,
    phoneNumber = this.phoneNumber
)

fun CreateUser.toUser(): User =
    User(
        name = this.name,
        pin = HashUtils.sha256(this.pin),
        phoneNumber = this.phoneNumber
    )

fun User.toReturnUserInfo():ReturnUserInfo =
    ReturnUserInfo(
        name = this.name,
        phoneNumber = this.phoneNumber
    )
