package com.mercadolivro.configuration.security

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Claim
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

    fun isValidToken(token: String): Boolean {
        val claims = getClaims(token)
        val subject = claims[ClaimType.SUBJECT.key]
        val expiration = claims[ClaimType.EXPIRATION.key]

        if (subject == null || expiration == null || Date().before(expiration.asDate())) {
            return false
        }

        return true
    }

    fun getSubject(token: String): String {
        val claims = getClaims(token)
        val subject = claims[ClaimType.SUBJECT.key] ?: return ""
        return subject.asString()
    }

    private fun getClaims(token: String): Map<String, Claim> {
        return JWT.decode(token).claims
    }

    private enum class ClaimType(
        val key: String
    ) {
        SUBJECT("sub"),
        EXPIRATION("exp")
    }

}