package com.mercadolivro.service

import com.mercadolivro.model.CustomerModel
import org.springframework.stereotype.Service

@Service
class CustomerService {

    fun getAllCustomers(name: String?): List<CustomerModel> {
        return listOf(CustomerModel("1", "Test", "test@test.com"))
    }

    fun getCustomer(id: String): CustomerModel {
        return CustomerModel("1", "Test", "test@test.com")
    }

    fun saveCustomer(customer: CustomerModel): CustomerModel {
        return CustomerModel("1", customer.name, customer.email)
    }

    fun updateCustomer(customer: CustomerModel): CustomerModel {
        return CustomerModel("1", customer.name, customer.email)
    }

    fun deleteCustomer(id: String) {}

}