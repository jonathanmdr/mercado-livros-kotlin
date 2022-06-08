package com.mercadolivro.extension

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel(): CustomerModel {
    return CustomerModel(
        name = this.name!!,
        email = this.email!!,
        status = CustomerStatus.ACTIVE
    )
}

fun PutCustomerRequest.toCustomerModel(customer: CustomerModel): CustomerModel {
    return CustomerModel(
        id = customer.id,
        name = this.name!!,
        email = this.email!!,
        status = customer.status
    )
}

fun PostBookRequest.toBookModel(customer: CustomerModel): BookModel {
    return BookModel(
        name = this.name!!,
        price = this.price!!,
        status = BookStatus.ACTIVE,
        customer = customer
    )
}

fun PutBookRequest.toBookModel(book: BookModel): BookModel {
    return BookModel(
        id = book.id,
        name = this.name ?: book.name,
        price = this.price ?: book.price,
        status = book.status,
        customer = book.customer
    )
}

fun CustomerModel.toResponseModel(): CustomerResponse {
    return CustomerResponse(
        id = this.id!!,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toResponseModel(): BookResponse {
    return BookResponse(
        id = this.id!!,
        name = this.name,
        price = this.price,
        status = this.status!!,
        customer = this.customer!!.toResponseModel()
    )
}