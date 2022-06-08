package com.mercadolivro.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.PropertyBindingException
import com.mercadolivro.controller.response.ErrorResponse
import com.mercadolivro.controller.response.FieldErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.BindException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Request body is invalid",
            exception.bindingResult.fieldErrors.map {
                FieldErrorResponse(
                    it.defaultMessage ?: "invalid",
                    it.field
                )
            }
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(BindException::class)
    fun handleBindException(exception: BindException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "Request body is invalid",
            exception.bindingResult.fieldErrors.map {
                FieldErrorResponse(
                    it.defaultMessage ?: "invalid",
                    it.field
                )
            }
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleHttpMessageNotReadableException(exception: HttpMessageNotReadableException, request: WebRequest): ResponseEntity<ErrorResponse> {
        return when (exception.rootCause) {
            is InvalidFormatException -> handleInvalidFormatException(exception.rootCause as InvalidFormatException, request)
            is PropertyBindingException -> handlePropertyBindingException(exception.rootCause as PropertyBindingException, request)
            is MethodArgumentTypeMismatchException -> handleMethodArgumentTypeMismatchException(exception.rootCause as MethodArgumentTypeMismatchException, request)
            else -> {
                ResponseEntity(
                    ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(),
                        "Request body is invalid"
                    ),
                    HttpStatus.UNPROCESSABLE_ENTITY
                )
            }
        }
    }

    @ExceptionHandler(InvalidFormatException::class)
    fun handleInvalidFormatException(exception: InvalidFormatException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "The property '%s' received value '%s', which is an invalid value.\nInform the compatible value with data type %s."
                .format(
                    exception.pathReference,
                    exception.value,
                    exception.targetType.simpleName
                )
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(PropertyBindingException::class)
    fun handlePropertyBindingException(exception: PropertyBindingException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "The property '%s' not found".format(exception.pathReference)
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException, request: WebRequest): ResponseEntity<ErrorResponse> {
        val error = ErrorResponse(
            HttpStatus.UNPROCESSABLE_ENTITY.value(),
            "The URL parameter '%s' received value '%s', which is an invalid value.\nInform the compatible value with data type %s."
                .format(
                    exception.name,
                    exception.value,
                    exception.requiredType?.simpleName
                )
        )

        return ResponseEntity(error, HttpStatus.UNPROCESSABLE_ENTITY)
    }

}