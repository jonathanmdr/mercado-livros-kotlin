package com.mercadolivro.validation

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.service.CustomerService
import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomerAvailableValidator(
    private val customerService: CustomerService
) : ConstraintValidator<CustomerAvailable, Int> {

    override fun isValid(consumerId: Int?, context: ConstraintValidatorContext?): Boolean {
        if (consumerId == null) {
            return false
        }

        val customer: CustomerModel

        try {
             customer = customerService.getCustomerById(consumerId)
        } catch (exception: EntityNotFoundException) {
            return false
        }

        return CustomerStatus.DEACTIVATED != customer.status
    }

}
