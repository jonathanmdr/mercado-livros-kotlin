package com.mercadolivro.controller.request

import com.mercadolivro.validation.CustomerAvailable
import javax.validation.constraints.NotNull

data class CustomerIdRequest(
    @field:NotNull
    @CustomerAvailable
    var id: Int?
)