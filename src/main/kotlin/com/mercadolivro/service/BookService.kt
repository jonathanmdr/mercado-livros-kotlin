package com.mercadolivro.service

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.BookRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class BookService(
    val bookRepository: BookRepository
) {

    fun getAllBooks(): List<BookModel> {
        return bookRepository.findAll()
    }

    fun getBookById(id: Int): BookModel {
        return bookRepository.findById(id)
            .orElseThrow()
    }

    fun saveBook(book: BookModel): BookModel {
        return bookRepository.save(book)
    }

    fun updateBook(book: BookModel): BookModel {
        if (!bookRepository.existsById(book.id!!)) {
            throw EntityNotFoundException()
        }

        return bookRepository.save(book)
    }

    fun deleteBook(id: Int) {
        if (!bookRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        var book = getBookById(id)
        book.status = BookStatus.DELETED
        bookRepository.save(book)
    }

    fun deleteByCustomer(customer: CustomerModel) {
        var books = bookRepository.findByCustomer(customer)
        books.forEach {
            if (BookStatus.ACTIVE == it.status) {
                it.status = BookStatus.DELETED
            }
        }
        bookRepository.saveAll(books)
    }
}