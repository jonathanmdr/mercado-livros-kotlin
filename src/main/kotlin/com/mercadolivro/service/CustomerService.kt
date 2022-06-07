package com.mercadolivro.service

import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityNotFoundException

@Transactional
@Service
class CustomerService(
    val customerRepository: CustomerRepository
) {

    fun getAllCustomers(name: String?): List<CustomerModel> {
        name?.let {
            return customerRepository.findByNameContaining(name)
        }
        return customerRepository.findAll()
    }

    fun getCustomer(id: Int): CustomerModel {
        return customerRepository.findById(id)
            .orElseThrow()
    }

    fun saveCustomer(customer: CustomerModel): CustomerModel {
        return customerRepository.save(customer)
    }

    fun updateCustomer(customer: CustomerModel): CustomerModel {
        if (!customerRepository.existsById(customer.id!!)) {
            throw EntityNotFoundException()
        }

        return customerRepository.save(customer)
    }

    fun deleteCustomer(id: Int) {
        if (!customerRepository.existsById(id)) {
            throw EntityNotFoundException()
        }

        customerRepository.deleteById(id)
    }

}