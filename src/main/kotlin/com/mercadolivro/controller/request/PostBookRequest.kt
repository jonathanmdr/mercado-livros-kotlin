package com.mercadolivro.controller.request

import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostBookRequest(
    @NotBlank
    var name: String,

    @NotNull
    @Positive
    var price: BigDecimal,

    @Valid
    @NotNull
    var customer: CustomerIdRequest
)