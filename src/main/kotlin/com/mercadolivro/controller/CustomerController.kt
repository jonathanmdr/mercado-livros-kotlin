package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toResponseModel
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/customers")
class CustomerController(
    private val customerService: CustomerService
) {

    @GetMapping
    fun getAllCustomers(@RequestParam(required = false) name: String?): ResponseEntity<List<CustomerResponse>> {
        return ResponseEntity.ok(customerService.getAllCustomers(name).map {
            it.toResponseModel()
        })
    }

    @GetMapping("/{id}")
    fun getCustomer(@PathVariable id: Int): ResponseEntity<CustomerResponse> {
        return ResponseEntity.ok(customerService.getCustomerById(id).toResponseModel())
    }

    @PostMapping
    fun saveCustomer(@RequestBody @Valid customer: PostCustomerRequest): ResponseEntity<CustomerResponse> {
        val saveCustomer = customerService.saveCustomer(customer.toCustomerModel())
        return ResponseEntity.created(ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand(saveCustomer.id).toUri())
            .body(saveCustomer.toResponseModel())
    }

    @PutMapping("/{id}")
    fun updateCustomer(@PathVariable id: Int, @RequestBody @Valid customer: PutCustomerRequest): ResponseEntity<CustomerResponse> {
        val previousCustomer = customerService.getCustomerById(id)
        val updateCustomer = customer.toCustomerModel(previousCustomer)
        return ResponseEntity.ok(customerService.updateCustomer(updateCustomer).toResponseModel())
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int) {
        customerService.deleteCustomer(id)
    }

}