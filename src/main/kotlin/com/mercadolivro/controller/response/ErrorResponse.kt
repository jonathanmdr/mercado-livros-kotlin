package com.mercadolivro.controller.response

data class ErrorResponse(
    val status: Int,
    val message: String,
    var errors: List<FieldErrorResponse>? = null
)