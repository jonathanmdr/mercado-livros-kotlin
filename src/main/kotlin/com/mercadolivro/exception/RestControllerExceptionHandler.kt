package com.mercadolivro.exception

import com.mercadolivro.controller.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import javax.persistence.EntityNotFoundException

@RestControllerAdvice
class RestControllerExceptionHandler {

    @ExceptionHandler(Exception::class)
    fun handleUncaughtException(exception: Exception, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "An unexpected internal error occurred"
        )

        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    fun handleEntityNotFoundException(exception: EntityNotFoundException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exception.message ?: ""
        )

        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(IllegalStateException::class)
    fun handleIllegalStateException(exception: IllegalStateException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            exception.message ?: ""
        )

        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

}