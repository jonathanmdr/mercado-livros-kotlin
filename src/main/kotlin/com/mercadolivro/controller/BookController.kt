package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.extension.toBookModel
import com.mercadolivro.extension.toResponseModel
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
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
import javax.validation.Valid

@RestController
@RequestMapping("/books")
class BookController(
    private val bookService: BookService,
    private val customerService: CustomerService
) {

    @GetMapping
    fun getAllBooks(@PageableDefault(page = 0, size = 10) pageable: Pageable): ResponseEntity<Page<BookResponse>> {
        return ResponseEntity.ok(bookService.getAllBooks(pageable)
            .map {
                it.toResponseModel()
            })
    }

    @GetMapping("/{id}")
    fun getBook(@PathVariable id: Int): ResponseEntity<BookResponse> {
        return ResponseEntity.ok(bookService.getBookById(id).toResponseModel())
    }

    @PostMapping
    fun saveBook(@RequestBody @Valid book: PostBookRequest): ResponseEntity<BookResponse> {
        val customer = customerService.getCustomerById(book.customer!!.id!!)
        val saveBook = bookService.saveBook(book.toBookModel(customer))
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(saveBook.id).toUri())
            .body(saveBook.toResponseModel())
    }

    @PatchMapping("/{id}")
    fun updateBook(@PathVariable id: Int, @RequestBody @Valid book: PutBookRequest): ResponseEntity<BookResponse> {
        val previousBook = bookService.getBookById(id)
        val updateBook = book.toBookModel(previousBook)
        return ResponseEntity.ok(bookService.updateBook(updateBook).toResponseModel())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteBook(@PathVariable id: Int) {
        bookService.deleteBook(id)
    }

}