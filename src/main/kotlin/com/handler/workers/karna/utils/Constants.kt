package com.handler.workers.karna.utils

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class Constants {

    object Messages {
        const val USER_CREATED_SUCCESSFULLY = "User created successfully"
        const val USER_INFO_SUCCESSFULLY_FETCHED = "Received User info successfully"
        const val INVALID_TOKEN_OR_TOKEN_EXPIRED = "Invalid token or Token expired"
        const val ALREADY_SIGNUP = "Already signed up, try login"
        const val SUCCESSFULLY_SIGNUP = "Successfully SignedUp"
        const val SUCCESSFULLY_LOGGED_IN = "Successfully Logged in"
        const val FAILED_TO_SIGNUP = "Failed to Signup"
        const val FAILED_TO_LOGIN = "Failed to Login"
        const val INVALID_CREDENTIALS = "Invalid credentials"
    }

    object DBConstants {
        const val DB_NAME = "constructionDb"
        const val USER_COLLECTION = "user"
        const val PROJECT_COLLECTION = "project"
        const val PROJECT_OWNER_COLLECTION = "project_owner"
    }

    object ApiNames {
        const val USER_BASE = "api/user"
        const val USER_CREATE = "create"
        const val USER_INFO = "info"
        const val USER_LOGIN = "login"
        const val WORKERS_BASE = "api/workers"
        const val ADD_WORKER = "add"
        const val GET_WORKERS_LIST = "list"
        const val GET_SINGLE_WORKER_INFO = "info"
        const val DELETE_WORKER = "delete"
    }


}