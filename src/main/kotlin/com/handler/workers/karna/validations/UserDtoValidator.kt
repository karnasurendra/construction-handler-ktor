package com.handler.workers.karna.validations

import com.handler.workers.karna.api.UserDto
import com.handler.workers.karna.api.UserLoginDto


class UserDtoValidator {

    fun validate(userDto: UserDto): List<String> {
        val validationErrors = mutableListOf<String>()

        if (userDto.userName.length < 6 || userDto.userName.length > 25) {
            validationErrors.add("Username must be at in between 6 and 25 characters")
        }

        if (userDto.mobileNumber.length != 10) {
            validationErrors.add("Mobile number must be 10 characters long.")
        }

        if (userDto.pin.length != 4) {
            validationErrors.add("PIN must be 4 characters long.")
        }

        return validationErrors
    }

    fun userLoginValidate(userLoginDto: UserLoginDto): List<String> {
        val validationErrors = mutableListOf<String>()


        if (userLoginDto.mobileNumber.length != 10) {
            validationErrors.add("Mobile number must be 10 characters long.")
        }

        if (userLoginDto.pin.length != 4) {
            validationErrors.add("PIN must be 4 characters long.")
        }

        return validationErrors
    }

}