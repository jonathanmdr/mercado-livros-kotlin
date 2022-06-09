package com.mercadolivro.configuration.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.util.Date

@Component
class JtwUtil {

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun generateToken(id: Int): String {
        return JWT.create()
            .withSubject(id.toString())
            .withExpiresAt(Date(System.currentTimeMillis() + expiration!!))
            .sign(Algorithm.HMAC512(secret))
    }

}