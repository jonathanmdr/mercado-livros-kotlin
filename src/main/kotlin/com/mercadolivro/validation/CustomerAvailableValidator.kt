package com.mercadolivro.validation

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.service.CustomerService
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomerAvailableValidator(
    private val customerService: CustomerService
) : ConstraintValidator<CustomerAvailable, Int> {

    override fun isValid(consumerId: Int?, context: ConstraintValidatorContext?): Boolean {
        if (consumerId == null) {
            return false
        }

        return CustomerStatus.DEACTIVATED != customerService.getCustomerById(consumerId).status
    }

}
