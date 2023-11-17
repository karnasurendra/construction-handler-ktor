package com.handler.workers.karna.utils

class Constants {

    object Messages {
        const val INVALID_TOKEN_OR_TOKEN_EXPIRED = "Invalid token or Token expired"
        const val ALREADY_SIGNUP = "Already signed up, try login"
        const val SUCCESSFULLY_SIGNUP = "Successfully SignedUp"
        const val SUCCESSFULLY_LOGGED_IN = "Successfully Logged in"
        const val FAILED_TO_SIGNUP = "Failed to Signup"
        const val FAILED_TO_LOGIN = "Failed to Login"
        const val INVALID_CREDENTIALS = "Invalid credentials"
    }

    object ValidationMessages {
        const val SUCCESS = "Success"
        const val VALIDATION_SUCCESS = "Validation success"
        const val INVALID_PHONE = "Invalid Phone number"
        const val INVALID_PIN = "Invalid Pin"
        const val INVALID_NAME = "Invalid Name"
        const val FAILED_TO_FETCH_USER_INFO = "Failed to fetch user info"
    }

}