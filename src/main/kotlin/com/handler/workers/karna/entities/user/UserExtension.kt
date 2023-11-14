package com.handler.workers.karna.entities.user


fun User.toDto(): CreateUser = CreateUser(
    id = this.id.toString(),
    name = this.name,
    mPin = this.mPin,
    phoneNumber = this.phoneNumber
)

fun CreateUser.toUser(): User =
    User(
        name = this.name,
        mPin = this.mPin,
        phoneNumber = this.phoneNumber
    )
