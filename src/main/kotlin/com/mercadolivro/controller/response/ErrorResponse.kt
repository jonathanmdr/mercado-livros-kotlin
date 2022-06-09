package com.mercadolivro.controller.response

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include

@JsonInclude(Include.NON_NULL)
data class ErrorResponse(
    val status: Int,
    val message: String,
    var errors: List<FieldErrorResponse>? = null
)