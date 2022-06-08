package com.mercadolivro.controller.request

import com.mercadolivro.validation.EmailAvailable
import javax.validation.constraints.NotBlank

data class PutCustomerRequest(
    @field:NotBlank
    var name: String?,

    @field:NotBlank
    @EmailAvailable
    var email: String?
)