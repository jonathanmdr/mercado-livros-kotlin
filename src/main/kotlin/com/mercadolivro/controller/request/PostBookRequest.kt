package com.mercadolivro.controller.request

import java.math.BigDecimal
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class PostBookRequest(
    @field:NotBlank
    var name: String?,

    @field:NotNull
    @field:Positive
    var price: BigDecimal?,

    @field:Valid
    @field:NotNull
    var customer: CustomerIdRequest?
)