package com.handler.workers.karna.utils

sealed class ApiResult<out T> {
    data class Failure(val errorCode: ErrorCode, val errorMessage: String) : ApiResult<Nothing>()
    data class Success<T>(val value: T) : ApiResult<T>()
}

enum class ErrorCode {
    PERSISTENCE_FAILURE,
    USER_NOT_FOUND,
    ATTENDANCE_NOT_FOUND,
    AUTHENTICATION_FAILURE,
    ENCRYPTION_FAILURE,
    USER_ALREADY_EXIST,
    INTEGRITY_CHECK_FAILED,
    MISSING_PARAMETER,
    INPUT_VALIDATION_FAILED,
    NOT_ACCEPTED_MIME_TYPE,
    DESERIALIZATION_ERROR,
    UNKNOWN_ERROR,
    AUTHORIZATION_FAILURE
}
