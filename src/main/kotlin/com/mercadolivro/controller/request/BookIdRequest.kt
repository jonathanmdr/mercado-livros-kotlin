package com.mercadolivro.controller.request

import com.mercadolivro.validation.BookAvailable
import javax.validation.constraints.NotNull

data class BookIdRequest(
    @field:NotNull
    @BookAvailable
    val id: Int?
)