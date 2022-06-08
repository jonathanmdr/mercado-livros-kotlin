package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.model.BookModel
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/books")
class BookController(
    val bookService: BookService,
    val customerService: CustomerService
) {

    @GetMapping
    fun getAllBooks(): ResponseEntity<List<BookModel>> {
        return ResponseEntity.ok(bookService.getAllBooks())
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Int): ResponseEntity<BookModel> {
        return ResponseEntity.ok(bookService.getBookById(id))
    }

    @PostMapping
    fun saveBook(@RequestBody book: PostBookRequest): ResponseEntity<BookModel> {
        val customer = customerService.getCustomerById(book.customer.id)
        val saveBook = bookService.saveBook(book.toBookModel(customer))
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(saveBook.id).toUri()).body(saveBook)
    }

    @PatchMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody book: PutBookRequest): ResponseEntity<BookModel> {
        val previousBook = bookService.getBookById(id)
        val updateBook = book.toBookModel(previousBook)
        return ResponseEntity.ok(bookService.updateBook(updateBook))
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) {
        bookService.deleteBook(id)
    }

}