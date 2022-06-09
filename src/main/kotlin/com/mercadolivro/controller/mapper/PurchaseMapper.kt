package com.mercadolivro.controller.mapper

import com.mercadolivro.controller.request.PostPurchaseRequest
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper(
    private val customerService: CustomerService,
    private val bookService: BookService
) {

    fun toModel(request: PostPurchaseRequest): PurchaseModel {
        val customer = customerService.getCustomerById(request.customer!!.id!!)
        val books = bookService.getAllByIds(request.books!!.map { it.id }.toList() as List<Int>)

        return PurchaseModel(
            customer = customer,
            books = books,
            total = books.sumOf { it.price }
        )
    }

}