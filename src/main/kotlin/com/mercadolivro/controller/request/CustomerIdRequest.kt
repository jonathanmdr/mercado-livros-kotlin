package com.mercadolivro.controller.request

import javax.validation.constraints.NotNull

data class CustomerIdRequest(
    @field:NotNull
    var id: Int?
)