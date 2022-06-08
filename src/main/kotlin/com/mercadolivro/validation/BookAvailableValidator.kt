package com.mercadolivro.validation

import com.mercadolivro.enums.BookStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.service.BookService
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class BookAvailableValidator(
    private val bookService: BookService
) : ConstraintValidator<BookAvailable, Int> {

    override fun isValid(bookId: Int?, context: ConstraintValidatorContext?): Boolean {
        if (bookId == null) {
            return false
        }

        val book: BookModel

        try {
            book = bookService.getBookById(bookId)
        } catch (exception: EntityNotFoundException) {
            return false
        }

        return BookStatus.ACTIVE == book.status
    }

}
