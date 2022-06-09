package com.mercadolivro.configuration.security

import com.mercadolivro.exception.AuthenticationException
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthorizationFilter(
    private val authManager: AuthenticationManager,
    private val userDetailsService: UserDetailsService,
    private val jwtUtil: JtwUtil
): BasicAuthenticationFilter(authManager) {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            val auth = getAuthentication(jwtToken.split(" ")[2])
            SecurityContextHolder.getContext().authentication = auth
        }
        chain.doFilter(request, response)
    }

    private fun getAuthentication(token: String): UsernamePasswordAuthenticationToken {
        val errorMessage = "JWT token is invalid"

        if (jwtUtil.isValidToken(token)) {
            throw AuthenticationException(errorMessage)
        }

        val subject = jwtUtil.getSubject(token)

        if (subject.isNullOrBlank()) {
            throw AuthenticationException(errorMessage)
        }

        val customer = userDetailsService.loadUserByUsername(subject)
        return UsernamePasswordAuthenticationToken(customer, null, customer.authorities)
    }

}