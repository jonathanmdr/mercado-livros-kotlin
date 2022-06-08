package com.mercadolivro.controller.response

import com.mercadolivro.enums.BookStatus
import java.math.BigDecimal

class BookResponse(
    val id: Int,
    val name: String,
    val price: BigDecimal,
    val status: BookStatus,
    val customer: CustomerResponse
)
