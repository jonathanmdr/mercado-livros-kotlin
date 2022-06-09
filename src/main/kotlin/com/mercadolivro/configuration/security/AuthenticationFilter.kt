package com.mercadolivro.configuration.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.request.AuthRequest
import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.repository.CustomerRepository
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter(
    private val authManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JtwUtil
) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        try {
            val authRequest = jacksonObjectMapper().readValue(request.inputStream, AuthRequest::class.java)
            val customerId = customerRepository.findByEmail(authRequest.email)?.id
            val authToken = UsernamePasswordAuthenticationToken(customerId, authRequest.password)
            return authManager.authenticate(authToken)
        } catch (exception: Exception) {
            throw AuthenticationException("Email or password is invalid")
        }
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication
    ) {
        val id = (authResult.principal as UserCustomDetails).id
        val jwtToken = jwtUtil.generateToken(id)
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer $jwtToken")
    }

}