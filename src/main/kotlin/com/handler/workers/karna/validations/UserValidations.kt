package com.handler.workers.karna.validations

import com.handler.workers.karna.entities.user.CreateUser
import com.handler.workers.karna.entities.user.User
import com.handler.workers.karna.utils.Constants

class UserValidations {

    data class ValidatedResponse(val isValid: Boolean, val message: String)

    fun validateUser(user: CreateUser): ValidatedResponse {

        if (!validatePhoneNumber(user.phoneNumber)) {
            return ValidatedResponse(false, Constants.ValidationMessages.INVALID_PHONE)
        }

        if (!validatePIN(user.pin)) {
            return ValidatedResponse(false, Constants.ValidationMessages.INVALID_PIN)
        }

        if (!validateName(user.name)) {
            return ValidatedResponse(false, Constants.ValidationMessages.INVALID_NAME)
        }

        return ValidatedResponse(true, Constants.ValidationMessages.VALIDATION_SUCCESS)

    }

    fun validateNumberAndPinOnly(user: CreateUser): ValidatedResponse {
        if (!validatePhoneNumber(user.phoneNumber)) {
            return ValidatedResponse(false, Constants.ValidationMessages.INVALID_PHONE)
        }

        if (!validatePIN(user.pin)) {
            return ValidatedResponse(false, Constants.ValidationMessages.INVALID_PIN)
        }

        return ValidatedResponse(true, Constants.ValidationMessages.VALIDATION_SUCCESS)
    }

    private fun validatePhoneNumber(phoneNumber: Long): Boolean {
        val regex = Regex("[0-9]{10}")
        return regex.matches(phoneNumber.toString())
    }

    private fun validatePIN(pin: String): Boolean {
        val regex = Regex("[0-9]{4}")
        return regex.matches(pin)
    }

    private fun validateName(name: String): Boolean {
        return (name.isNotEmpty() && name.length > 5)
    }

}