package com.mercadolivro.controller.response

import com.mercadolivro.enums.CustomerStatus

data class CustomerResponse(
    val id: Int,
    val name: String,
    val email: String,
    val status: CustomerStatus
)
