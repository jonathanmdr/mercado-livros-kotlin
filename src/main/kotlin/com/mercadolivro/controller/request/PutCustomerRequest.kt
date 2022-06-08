package com.mercadolivro.controller.request

import javax.validation.constraints.NotBlank

data class PutCustomerRequest(
    @NotBlank
    var name: String,

    @NotBlank
    var email: String
)