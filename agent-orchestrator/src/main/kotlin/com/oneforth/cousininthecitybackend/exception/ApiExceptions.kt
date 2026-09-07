package com.oneforth.cousininthecitybackend.exception

import java.time.LocalDateTime

data class ApiErrorResponse(
    val type: String,
    val message: String,
    val possibleCause: String?,
    val timestamp: LocalDateTime = LocalDateTime.now()
)

open class ApiException(
    message: String,
    val type: String = "INTERNAL_ERROR",
    val possibleCause: String? = null
) : RuntimeException(message)

class ResourceNotFoundException(message: String, cause: String? = null) : 
    ApiException(message, "RESOURCE_NOT_FOUND", cause)

class BadRequestException(message: String, cause: String? = null) : 
    ApiException(message, "BAD_REQUEST", cause)

fun Throwable.toApiErrorResponse(): ApiErrorResponse {
    return when (this) {
        is ApiException -> ApiErrorResponse(
            type = this.type,
            message = this.message ?: "An unexpected error occurred",
            possibleCause = this.possibleCause
        )
        is IllegalArgumentException -> ApiErrorResponse(
            type = "BAD_REQUEST",
            message = this.message ?: "Invalid argument provided",
            possibleCause = "Check the request payload and parameters."
        )
        else -> ApiErrorResponse(
            type = "UNKNOWN_SERVER_ERROR",
            message = this.localizedMessage ?: "An unknown server error occurred",
            possibleCause = "Please check server logs for more details."
        )
    }
}
