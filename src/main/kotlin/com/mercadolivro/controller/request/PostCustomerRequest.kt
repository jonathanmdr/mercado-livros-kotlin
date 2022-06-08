package com.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class PostCustomerRequest(
    @field:NotBlank
    var name: String?,

    @field:NotBlank
    @field:Email
    var email: String?
)