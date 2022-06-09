package com.mercadolivro.configuration.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.response.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class CustomAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(request: HttpServletRequest, response: HttpServletResponse, authException: AuthenticationException) {
        response.contentType = "application/json"
        response.status = HttpStatus.UNAUTHORIZED.value()
        val error = ErrorResponse(
            HttpStatus.UNAUTHORIZED.value(),
            authException.message ?: "Unauthorized"
        )
        response.outputStream.print(jacksonObjectMapper().writeValueAsString(error))
    }

}