package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Profile
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val bookService: BookService,
    private val passwordEncoder: PasswordEncoder
) {

    fun getAllCustomers(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(name)
        }
        return customerRepository.findAll()
    }

    fun getCustomerById(id: Int): CustomerModel {
        return customerRepository.findById(id)
            .orElseThrow {
                EntityNotFoundException("Customer $id not found")
            }
    }

    fun saveCustomer(customer: CustomerModel): CustomerModel {
        val customerToSave = customer.copy(
            roles = setOf(Profile.CUSTOMER),
            password = passwordEncoder.encode(customer.password)
        )
        return customerRepository.save(customerToSave)
    }

    fun updateCustomer(customer: CustomerModel): CustomerModel {
        if (!customerRepository.existsById(customer.id!!)) {
            throw EntityNotFoundException("Customer ${customer.id} not found")
        }

        return customerRepository.save(customer)
    }

    fun deleteCustomer(id: Int) {
        val customer = getCustomerById(id)
        bookService.deleteByCustomer(customer)

        customer.status = CustomerStatus.DEACTIVATED

        customerRepository.save(customer)
    }

    fun emailIsAvailable(email: String): Boolean {
        return !customerRepository.existsByEmail(email)
    }
}