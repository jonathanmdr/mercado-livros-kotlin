package com.mercadolivro.controller.request

import javax.validation.constraints.NotBlank

data class PutCustomerRequest(
    @field:NotBlank
    var name: String?,

    @field:NotBlank
    var email: String?
)