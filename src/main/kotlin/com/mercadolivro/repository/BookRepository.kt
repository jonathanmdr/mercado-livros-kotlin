package com.mercadolivro.repository

import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface BookRepository : JpaRepository<BookModel, Int> {
    fun findByCustomer(customer: CustomerModel): List<BookModel>
}