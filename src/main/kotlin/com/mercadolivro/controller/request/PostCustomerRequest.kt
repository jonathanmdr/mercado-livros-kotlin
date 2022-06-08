package com.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

data class PostCustomerRequest(
    @NotBlank
    var name: String,

    @NotBlank
    @Email
    var email: String
)