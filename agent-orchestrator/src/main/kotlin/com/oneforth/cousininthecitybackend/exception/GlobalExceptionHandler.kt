package com.oneforth.cousininthecitybackend.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(ApiException::class)
    fun handleApiException(ex: ApiException): ResponseEntity<ApiErrorResponse> {
        logger.error("API Exception: ${ex.message}", ex)
        val status = when(ex.type) {
            "RESOURCE_NOT_FOUND" -> HttpStatus.NOT_FOUND
            "BAD_REQUEST" -> HttpStatus.BAD_REQUEST
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }
        return ResponseEntity(ex.toApiErrorResponse(), status)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ApiErrorResponse> {
        logger.error("Illegal Argument: ${ex.message}", ex)
        return ResponseEntity(ex.toApiErrorResponse(), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(Exception::class)
    fun handleGenericException(ex: Exception): ResponseEntity<ApiErrorResponse> {
        logger.error("Unhandled Exception: ${ex.message}", ex)
        return ResponseEntity(ex.toApiErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR)
    }
}
