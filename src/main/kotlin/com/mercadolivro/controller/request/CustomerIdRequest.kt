package com.mercadolivro.controller.request

import javax.validation.constraints.NotNull

data class CustomerIdRequest(
    @NotNull
    var id: Int
)