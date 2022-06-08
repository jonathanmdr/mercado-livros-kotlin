package com.mercadolivro.controller.request

import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class PostPurchaseRequest(
    @field:Valid
    @field:NotNull
    val customer: CustomerIdRequest?,

    @field:Valid
    @field:NotEmpty
    val books: Set<BookIdRequest>?

)
