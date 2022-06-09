package com.mercadolivro.service

import com.mercadolivro.configuration.security.UserCustomDetails
import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow { AuthenticationException("Email or password is invalid") }

        return UserCustomDetails(customer)
    }

}