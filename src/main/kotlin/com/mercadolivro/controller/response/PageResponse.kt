package com.mercadolivro.controller.response

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class PageResponse<T>(
    val items: List<T>,
    val page: Int,
    val totalItems: Long,
    val totalPages: Int
)