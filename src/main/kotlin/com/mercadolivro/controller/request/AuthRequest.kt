package com.mercadolivro.controller.request

data class AuthRequest(
    val email: String,
    val password: String
)