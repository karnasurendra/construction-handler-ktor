package com.handler.workers.karna.services

import com.handler.workers.karna.api.UserDto
import com.handler.workers.karna.api.UserLoginDto
import com.handler.workers.karna.domain.User
import com.handler.workers.karna.domain.defaultSecurityFileSettings
import com.handler.workers.karna.persistance.UserRepository
import com.handler.workers.karna.security.EncryptionManager
import com.handler.workers.karna.security.JWTConfig
import com.handler.workers.karna.utils.ApiResult
import com.handler.workers.karna.utils.ErrorCode
import com.handler.workers.karna.validations.UserDtoValidator

interface UserService {
    suspend fun save(userDto: UserDto): ApiResult<String>
    suspend fun findById(id: String): ApiResult<User>
    suspend fun login(userLoginDto: UserLoginDto): ApiResult<String>

}

class UserServiceImpl(
    private val userDtoValidator: UserDtoValidator,
    private val userRepository: UserRepository
) : UserService {

    override suspend fun save(userDto: UserDto): ApiResult<String> {
        try {

            val validationResults = userDtoValidator.validate(userDto)
            if (validationResults.isNotEmpty()) {
                throw ApplicationServiceException(
                    ErrorCode.MISSING_PARAMETER,
                    validationResults.joinToString(",")
                )
            }

            val user = userRepository.findBy(userDto.mobileNumber)
            if (user != null) {
                throw ApplicationServiceException(
                    ErrorCode.USER_ALREADY_EXIST,
                    "user already exists with ${userDto.mobileNumber}"
                )
            } else {
                val userId = userRepository.save(
                    User(
                        username = userDto.userName,
                        mobileNumber = userDto.mobileNumber,
                        pin = EncryptionManager.encryptString(userDto.pin),
                        securitySettings = defaultSecurityFileSettings(),
                        experienceInYears = userDto.experience,
                        expertiseIn = userDto.expertiseIn
                    )
                )
                return if (userId != null) {
                    val jwtToken = JWTConfig.makeToken(userId)
                    ApiResult.Success(jwtToken)
                } else {
                    ApiResult.Failure(ErrorCode.PERSISTENCE_FAILURE, "Failed to create user")
                }

            }
        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }
    }

    override suspend fun findById(id: String): ApiResult<User> {
        return try {
            val user = userRepository.findUserById(id)
            if (user != null) {
                ApiResult.Success(user)
            } else {
                ApiResult.Failure(ErrorCode.PERSISTENCE_FAILURE, "Failed to find user")
            }
        } catch (e: GeneralException) {
            ApiResult.Failure(ErrorCode.USER_NOT_FOUND, "Failed to find user")
        }
    }

    override suspend fun login(userLoginDto: UserLoginDto): ApiResult<String> {
        try {
            val validationResult = userDtoValidator.userLoginValidate(userLoginDto)
            if (validationResult.isNotEmpty()) {
                throw ApplicationServiceException(
                    ErrorCode.MISSING_PARAMETER,
                    validationResult.joinToString(",")
                )
            }

            val existingUser = userRepository.findBy(userLoginDto.mobileNumber)
                ?: throw ApplicationServiceException(
                    ErrorCode.INPUT_VALIDATION_FAILED,
                    "No user found with ${userLoginDto.mobileNumber}"
                )

            val decryptedPin = EncryptionManager.decryptString(existingUser.pin, existingUser.securitySettings, userLoginDto.pin)

            if (decryptedPin != userLoginDto.pin){
                throw ApplicationServiceException(
                    ErrorCode.AUTHENTICATION_FAILURE,
                    "Invalid pin."
                )
            }

            return if (existingUser.id != null) {
                val jwtToken = JWTConfig.makeToken(existingUser.id)
                ApiResult.Success(jwtToken)
            } else {
                ApiResult.Failure(ErrorCode.PERSISTENCE_FAILURE, "Login successful")
            }


        } catch (e: GeneralException) {
            return ApiResult.Failure(e.errorCode, e.message)
        }

    }

}