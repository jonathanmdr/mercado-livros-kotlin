package com.mercadolivro.controller.request

import java.math.BigDecimal

data class PostBookRequest(
    var name: String,
    var price: BigDecimal,
    var customer: CustomerIdRequest
)